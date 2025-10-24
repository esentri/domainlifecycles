# Chapter 2 – Understanding DLC Configuration

---

Covered topics: Auto-Configuration

---

Before we dive into domain modeling, let’s take a quick look at how DLC integrates into our project.

## Auto-Configuration

DLC comes with smart Spring Boot auto-configuration.
This means you don’t have to wire repositories, persistence, or event publishers manually.

The annotation you’ve already used, `@EnableDlc`, tells DLC:

- Where to find your domain model (dlcDomainBasePackages)
- How to configure persistence (if JOOQ is present)
- How to enable domain event handling

`@EnableDlc(dlcDomainBasePackages = "com.shop.domain")`

**That’s all you need for a clean start.**

## What Happens Behind the Scenes

When your application starts, DLC automatically:

- Scans the provided domain packages for aggregates, entities, and value objects. 
- Registers default repositories and persistence adapters (if the persistence module is on the classpath). 
- Sets up the domain event bus for internal and external events.

You can later customize these behaviors, but for now the defaults are perfect for getting started.

## Why This Matters for DDD

In traditional DDD projects, you’d spend time wiring persistence logic, repositories, and transaction boundaries manually.
DLC handles that for you — so your focus stays on the domain language: Products, Orders, and Customers.

With this, our foundation is ready.
It’s time to move from theory to practice — and start building Emma’s first aggregate: the Product.

---

| **Chapter 1 - Creating the Project** | **Chapter 3 - Product**  |
|:------------------------------------:|:------------------------:|
|      [<< Previous](c0_intro.md)      | [Next >>](c3_product.md) |