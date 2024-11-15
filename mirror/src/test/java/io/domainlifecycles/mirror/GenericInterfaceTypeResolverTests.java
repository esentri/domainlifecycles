package io.domainlifecycles.mirror;

import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.ReadModel;
import io.domainlifecycles.mirror.reflect.GenericInterfaceTypeResolver;
import org.junit.jupiter.api.Test;
import tests.mirror.BaseEntityWithHidden;
import tests.mirror.DirectQueryClient;
import tests.mirror.DirectQueryClientBounded;
import tests.mirror.MyReadModel;
import tests.mirror.QueryClientImpl;
import tests.mirror.QueryClientImplSub;
import tests.mirror.QueryClientSubGeneric;
import tests.mirror.QueryClientInterface;
import tests.mirror.QueryClientInterfaceBounded;
import tests.mirror.QueryClientSubSub;
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
    public void testQueryClientInterfaceInterface() {
        var resolver = new GenericInterfaceTypeResolver(QueryClientInterface.class);
        var resolved = resolver.resolveFor(QueryClient.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testQueryClientInterfaceImpl() {
        var resolver = new GenericInterfaceTypeResolver(QueryClientImpl.class);
        var resolved = resolver.resolveFor(QueryClient.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testQueryClientInterfaceImplSub() {
        var resolver = new GenericInterfaceTypeResolver(QueryClientImplSub.class);
        var resolved = resolver.resolveFor(QueryClient.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testQueryClientInterfaceInterfaceBounded() {
        var resolver = new GenericInterfaceTypeResolver(QueryClientInterfaceBounded.class);
        var resolved = resolver.resolveFor(QueryClient.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testDirectQueryClient() {
        var resolver = new GenericInterfaceTypeResolver(DirectQueryClient.class);
        var resolved = resolver.resolveFor(QueryClient.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testDirectQueryClientBounded() {
        var resolver = new GenericInterfaceTypeResolver(DirectQueryClientBounded.class);
        var resolved = resolver.resolveFor(QueryClient.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testQueryClientSubSub() {
        var resolver = new GenericInterfaceTypeResolver(QueryClientSubSub.class);
        var resolved = resolver.resolveFor(QueryClient.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testQueryClientSubGeneric() {
        var resolver = new GenericInterfaceTypeResolver(QueryClientSubGeneric.class);
        var resolved = resolver.resolveFor(QueryClient.class, 0);
        assertThat(resolved).isEqualTo(ReadModel.class);
    }


}
