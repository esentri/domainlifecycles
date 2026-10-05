#!/usr/bin/env python3
"""
Generates the concept diagrams of concepts/readme.md: one scene - the application boundary, the domain and its
building blocks - drawn with a different subset of the building blocks and highlighted elements per diagram.

Usage: python3 generate_concept_diagrams.py
Writes the PNG of each diagram into documentation/resources/images, converting the SVG with rsvg-convert
(e.g. brew install librsvg).
"""
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

HERE = Path(__file__).resolve().parent
IMAGES = HERE.parent / "images"

WIDTH, HEIGHT = 944, 514
SCALE = 2

INK = "#555555"
TEXT = "#1f1f1f"
RED = "#E8412C"
FONT = "Helvetica, Arial, sans-serif"

# name: (x, y, width, height, fill)
BOXES = {
    "ApplicationService": (389, 80, 163, 24, "#EFE7D5"),
    "DomainCommand": (226, 140, 163, 24, "#FFAD5C"),
    "DomainService": (573, 140, 145, 24, "#E0E0E0"),
    "Factory": (139, 200, 123, 24, "#E0F0E0"),
    "ReadModel": (139, 257, 123, 24, "#FFCCE5"),
    "DomainEvent": (668, 268, 140, 24, "#CCFFFF"),
    "QueryHandler": (202, 396, 135, 24, "#BBBBBB"),
    "QueryHandlerImpl": (189, 455, 162, 24, "#FFFFFF"),
    "Repository": (400, 410, 116, 24, "#BBBBBB"),
    "RepositoryImpl": (377, 466, 162, 24, "#FFFFFF"),
    "OutboundService": (656, 354, 162, 24, "#BBBBBB"),
    "OutboundServiceImpl": (645, 425, 184, 24, "#FFFFFF"),
}
FRAME = (287, 197, 340, 165)
AGGREGATE_ROOT = (304, 231, 144, 53)
ENTITY = (382, 298, 149, 53)
VALUE_OBJECT = (494, 247, 123, 24)

CLASSIC = ["Aggregate", "DomainService", "Factory", "Repository", "RepositoryImpl"]
WITH_APPLICATION_SERVICE = CLASSIC + ["ApplicationService"]
WITH_EVENTS = WITH_APPLICATION_SERVICE + ["DomainEvent"]
WITH_COMMANDS = WITH_EVENTS + ["DomainCommand"]
WITH_READ_MODELS = WITH_COMMANDS + ["ReadModel", "QueryHandler", "QueryHandlerImpl"]
ALL = WITH_READ_MODELS + ["OutboundService", "OutboundServiceImpl"]

# the routes of the relations, as points; arrow: whether the last point gets an arrow head
ROUTES = {
    "DomainService-Repository": ([(645, 164), (645, 422), (516, 422)], False),
    "Factory-Aggregate": ([(262, 212), (287, 212)], True),
    "ApplicationService-Factory": ([(389, 92), (170, 92), (170, 200)], True),
    "DomainService-Factory": ([(600, 164), (600, 182), (240, 182), (240, 200)], True),
    "ApplicationService-Repository": ([(470, 80), (470, 56), (48, 56), (48, 422), (400, 422)], False),
    "ApplicationService-DomainService": ([(470, 56), (645, 56), (645, 140)], False),
    "ApplicationService-DomainEvent": ([(552, 93), (765, 93), (765, 268)], True),
    "DomainService-DomainEvent": ([(645, 164), (645, 218), (737, 218), (737, 268)], True),
    "Aggregate-DomainEvent": ([(627, 282), (668, 282)], True),
    "DomainEvent-Aggregate": ([(737, 292), (737, 320), (627, 320)], True),
    "DomainEvent-DomainService": ([(808, 282), (858, 282), (858, 155), (718, 155)], True),
    "DomainEvent-ApplicationService": ([(858, 155), (858, 56), (470, 56), (470, 80)], True),
    "ApplicationService-DomainCommand": ([(470, 104), (470, 122), (308, 122), (308, 140)], True),
    "DomainCommand-DomainService": ([(389, 152), (573, 152)], True),
    "DomainCommand-Aggregate": ([(308, 164), (308, 181), (458, 181), (458, 197)], True),
    "ApplicationService-QueryHandler": ([(470, 80), (470, 56), (92, 56), (92, 408), (202, 408)], False),
    "DomainService-QueryHandler": ([(645, 140), (645, 115), (864, 115), (864, 408), (337, 408)], False),
    "ApplicationService-OutboundService": ([(552, 92), (858, 92), (858, 366), (818, 366)], False),
    "DomainService-OutboundService": ([(645, 164), (645, 330), (737, 330), (737, 354)], False),
}

# file name: (shown elements, highlighted elements, highlighted routes)
DIAGRAMS = {
    "concept_overview": (ALL, [], []),
    "classic_ddd_building_blocks": (CLASSIC, CLASSIC, []),
    "domainservice_call_repository": (CLASSIC, [], ["DomainService-Repository"]),
    "factories": (WITH_APPLICATION_SERVICE, ["Factory"], ["Factory-Aggregate"]),
    "application_services": (WITH_APPLICATION_SERVICE, ["ApplicationService"], []),
    "applicationservice_call_domainservice_repository": (
        WITH_APPLICATION_SERVICE, [], ["ApplicationService-Repository", "ApplicationService-DomainService"]),
    "factory_access": (
        WITH_APPLICATION_SERVICE, [], ["ApplicationService-Factory", "DomainService-Factory", "Factory-Aggregate"]),
    "domain_events": (WITH_EVENTS, ["DomainEvent"], [
        "ApplicationService-DomainEvent", "DomainService-DomainEvent", "Aggregate-DomainEvent",
        "DomainEvent-Aggregate", "DomainEvent-DomainService", "DomainEvent-ApplicationService"]),
    "domain_commands": (WITH_COMMANDS, ["DomainCommand"], [
        "ApplicationService-DomainCommand", "DomainCommand-DomainService", "DomainCommand-Aggregate"]),
    "read_models_query_handlers": (WITH_READ_MODELS, ["ReadModel", "QueryHandler", "QueryHandlerImpl"], []),
    "query_handler_domain_service_application_service": (
        WITH_READ_MODELS, [], ["ApplicationService-QueryHandler", "DomainService-QueryHandler"]),
    "outbound_services": (ALL, ["OutboundService", "OutboundServiceImpl"], []),
    "outboundservice_domainservice_applicationservice": (
        ALL, [], ["ApplicationService-OutboundService", "DomainService-OutboundService"]),
}


def box(x, y, w, h, fill, label, highlighted, text_y=None):
    stroke = RED if highlighted else INK
    ty = text_y if text_y is not None else y + h / 2 + 4
    return (f'<rect x="{x}" y="{y}" width="{w}" height="{h}" fill="{fill}" stroke="{stroke}" stroke-width="1.5"/>'
            f'<text x="{x + w / 2}" y="{ty}" text-anchor="middle">&lt;&lt;{label}&gt;&gt;</text>')


def identity(x, y, w, highlighted):
    stroke = RED if highlighted else INK
    return (f'<rect x="{x}" y="{y}" width="{w}" height="16" fill="#EEEEEE" stroke="{stroke}" stroke-width="1.5"/>'
            f'<text x="{x + w / 2}" y="{y + 12}" text-anchor="middle">&lt;&lt;Identity&gt;&gt;</text>')


def line(points, color, dashed, arrow=False, hollow=False):
    path = " ".join(f"{x},{y}" for x, y in points)
    dash = ' stroke-dasharray="6 4"' if dashed else ""
    marker = ""
    if arrow:
        marker = ' marker-end="url(#arrow-red)"' if color == RED else ' marker-end="url(#arrow)"'
    if hollow:
        marker = ' marker-end="url(#inherit-red)"' if color == RED else ' marker-end="url(#inherit)"'
    return f'<polyline points="{path}" fill="none" stroke="{color}" stroke-width="1.5"{dash}{marker}/>'


def aggregate(highlighted):
    stroke = RED if highlighted else INK
    x, y, w, h = FRAME
    parts = [f'<rect x="{x}" y="{y}" width="{w}" height="{h}" fill="#E8E1C9" stroke="{stroke}" stroke-width="1.5"/>',
             f'<text x="{x + 10}" y="{y + 22}">&lt;&lt;Aggregate&gt;&gt;</text>']
    # the connections between the parts, drawn below them
    parts.append(line([(448, 259), (494, 259)], stroke, False))
    parts.append(line([(370, 284), (370, 326), (382, 326)], stroke, False))
    parts.append(line([(531, 322), (556, 322), (556, 271)], stroke, False))
    ax, ay, aw, ah = AGGREGATE_ROOT
    parts.append(box(ax, ay, aw, ah, "#7DFF6E", "AggregateRoot", highlighted, text_y=ay + 21))
    parts.append(identity(ax + 44, ay + ah - 16, aw - 44, highlighted))
    vx, vy, vw, vh = VALUE_OBJECT
    parts.append(box(vx, vy, vw, vh, "#FFFFC2", "ValueObject", highlighted))
    ex, ey, ew, eh = ENTITY
    parts.append(box(ex, ey, ew, eh, "#88AAFF", "Entity", highlighted, text_y=ey + 23))
    parts.append(identity(ex + 48, ey + eh - 16, ew - 48, highlighted))
    return parts


def render(shown, highlighted, routes):
    def red(name):
        return name in highlighted

    out = [f'<svg xmlns="http://www.w3.org/2000/svg" width="{WIDTH * SCALE}" height="{HEIGHT * SCALE}" '
           f'viewBox="0 0 {WIDTH} {HEIGHT}" font-family="{FONT}" font-size="12" fill="{TEXT}">',
           '<defs>']
    for marker_id, color in (("arrow", INK), ("arrow-red", RED)):
        out.append(f'<marker id="{marker_id}" viewBox="0 0 10 10" refX="10" refY="5" markerWidth="8" '
                   f'markerHeight="8" orient="auto-start-reverse" markerUnits="userSpaceOnUse">'
                   f'<path d="M0,0 L10,5 L0,10 z" fill="{color}"/></marker>')
    for marker_id, color in (("inherit", INK), ("inherit-red", RED)):
        out.append(f'<marker id="{marker_id}" viewBox="0 0 10 10" refX="10" refY="5" markerWidth="9" '
                   f'markerHeight="9" orient="auto" markerUnits="userSpaceOnUse">'
                   f'<path d="M0,0 L10,5 L0,10 z" fill="#FFFFFF" stroke="{color}" stroke-width="1.2"/></marker>')
    out.append('</defs>')
    out.append(f'<rect width="{WIDTH}" height="{HEIGHT}" fill="#FFFFFF"/>')
    out.append(f'<rect x="4" y="4" width="{WIDTH - 8}" height="{HEIGHT - 8}" rx="70" fill="#FFFFFF" '
               f'stroke="{INK}" stroke-width="1.5" stroke-dasharray="7 5"/>')
    out.append(f'<ellipse cx="471" cy="260" rx="372" ry="167" fill="#F8F9FB" stroke="{INK}" stroke-width="1.5" '
               f'stroke-dasharray="7 5"/>')

    # the structural relations, below the highlighted routes and the boxes
    def structural(points, a, b, hollow=False):
        if a in shown and b in shown:
            color = RED if red(a) and red(b) else INK
            out.append(line(points, color, not hollow, hollow=hollow))

    structural([(202, 281), (202, 338), (269, 338), (269, 396)], "ReadModel", "QueryHandler")
    structural([(458, 362), (458, 410)], "Aggregate", "Repository")
    structural([(270, 455), (270, 420)], "QueryHandlerImpl", "QueryHandler", hollow=True)
    structural([(458, 466), (458, 434)], "RepositoryImpl", "Repository", hollow=True)
    structural([(737, 425), (737, 378)], "OutboundServiceImpl", "OutboundService", hollow=True)

    for route in routes:
        points, arrow = ROUTES[route]
        out.append(line(points, RED, True, arrow=arrow))

    if "Aggregate" in shown:
        out.extend(aggregate(red("Aggregate")))
    for name, (x, y, w, h, fill) in BOXES.items():
        if name in shown:
            out.append(box(x, y, w, h, fill, name, red(name)))
    out.append('</svg>')
    return "\n".join(out)


def main():
    converter = shutil.which("rsvg-convert")
    if not converter:
        sys.exit("rsvg-convert is needed to convert the diagrams to PNG")
    with tempfile.TemporaryDirectory() as svg_dir:
        for name, (shown, highlighted, routes) in DIAGRAMS.items():
            svg_file = Path(svg_dir) / f"{name}.svg"
            svg_file.write_text(render(shown, highlighted, routes), encoding="utf-8")
            subprocess.run([converter, str(svg_file), "-o", str(IMAGES / f"{name}.png")], check=True)
            print(name)


if __name__ == "__main__":
    main()
