package io.domainlifecycles.mirror;

import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.QueryHandler;
import io.domainlifecycles.domain.types.ReadModel;
import io.domainlifecycles.mirror.reflect.GenericInterfaceTypeResolver;
import org.junit.jupiter.api.Test;
import tests.mirror.BaseEntityWithHidden;
import tests.mirror.DirectQueryHandler;
import tests.mirror.DirectQueryHandlerBounded;
import tests.mirror.MyReadModel;
import tests.mirror.QueryHandlerImpl;
import tests.mirror.QueryHandlerImplSub;
import tests.mirror.QueryHandlerInterface;
import tests.mirror.QueryHandlerInterfaceBounded;
import tests.mirror.QueryHandlerSubGeneric;
import tests.mirror.QueryHandlerSubSub;
import tests.mirror.SubEntityHiding;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericInterfaceTypeResolverTests {

    @Test
    public void testIdentityValueType() {
        var resolver = new GenericInterfaceTypeResolver(BaseEntityWithHidden.HiddenId.class);
        var resolved = resolver.resolveFor(Identity.class, 0);
        assertThat(resolved).isEqualTo(Long.class);
    }

    @Test
    public void testSubEntityHiding() {
        var resolver = new GenericInterfaceTypeResolver(SubEntityHiding.class);
        var resolved = resolver.resolveFor(Entity.class, 0);
        assertThat(resolved).isEqualTo(BaseEntityWithHidden.HiddenId.class);
    }

    @Test
    public void testBaseEntityWithHidden() {
        var resolver = new GenericInterfaceTypeResolver(BaseEntityWithHidden.class);
        var resolved = resolver.resolveFor(Entity.class, 0);
        assertThat(resolved).isEqualTo(BaseEntityWithHidden.HiddenId.class);
    }

    @Test
    public void testQueryHandlerInterfaceInterface() {
        var resolver = new GenericInterfaceTypeResolver(QueryHandlerInterface.class);
        var resolved = resolver.resolveFor(QueryHandler.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testQueryHandlerInterfaceImpl() {
        var resolver = new GenericInterfaceTypeResolver(QueryHandlerImpl.class);
        var resolved = resolver.resolveFor(QueryHandler.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testQueryHandlerInterfaceImplSub() {
        var resolver = new GenericInterfaceTypeResolver(QueryHandlerImplSub.class);
        var resolved = resolver.resolveFor(QueryHandler.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testQueryHandlerInterfaceInterfaceBounded() {
        var resolver = new GenericInterfaceTypeResolver(QueryHandlerInterfaceBounded.class);
        var resolved = resolver.resolveFor(QueryHandler.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testDirectQueryHandler() {
        var resolver = new GenericInterfaceTypeResolver(DirectQueryHandler.class);
        var resolved = resolver.resolveFor(QueryHandler.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testDirectQueryHandlerBounded() {
        var resolver = new GenericInterfaceTypeResolver(DirectQueryHandlerBounded.class);
        var resolved = resolver.resolveFor(QueryHandler.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testQueryHandlerSubSub() {
        var resolver = new GenericInterfaceTypeResolver(QueryHandlerSubSub.class);
        var resolved = resolver.resolveFor(QueryHandler.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testQueryHandlerSubGeneric() {
        var resolver = new GenericInterfaceTypeResolver(QueryHandlerSubGeneric.class);
        var resolved = resolver.resolveFor(QueryHandler.class, 0);
        assertThat(resolved).isEqualTo(ReadModel.class);
    }


}
