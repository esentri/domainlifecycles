package nitrox.dlc.mirror;

import nitrox.dlc.domain.types.Entity;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.domain.types.ReadModel;
import nitrox.dlc.domain.types.ReadModelProvider;
import nitrox.dlc.mirror.reflect.GenericInterfaceTypeResolver;
import org.junit.jupiter.api.Test;
import tests.mirror.BaseEntityWithHidden;
import tests.mirror.DirectReadModelProvider;
import tests.mirror.DirectReadModelProviderBounded;
import tests.mirror.MyReadModel;
import tests.mirror.ReadModelProviderImpl;
import tests.mirror.ReadModelProviderImplSub;
import tests.mirror.ReadModelProviderInterface;
import tests.mirror.ReadModelProviderInterfaceBounded;
import tests.mirror.ReadModelProviderSubGeneric;
import tests.mirror.ReadModelProviderSubSub;
import tests.mirror.SubEntityHiding;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericInterfaceTypeResolverTests {

    @Test
    public void testIdentityValueType(){
        var resolver = new GenericInterfaceTypeResolver(BaseEntityWithHidden.HiddenId.class);
        var resolved = resolver.resolveFor(Identity.class, 0);
        assertThat(resolved).isEqualTo(Long.class);
    }

    @Test
    public void testSubEntityHiding(){
        var resolver = new GenericInterfaceTypeResolver(SubEntityHiding.class);
        var resolved = resolver.resolveFor(Entity.class, 0);
        assertThat(resolved).isEqualTo(BaseEntityWithHidden.HiddenId.class);
    }

    @Test
    public void testBaseEntityWithHidden(){
        var resolver = new GenericInterfaceTypeResolver(BaseEntityWithHidden.class);
        var resolved = resolver.resolveFor(Entity.class, 0);
        assertThat(resolved).isEqualTo(BaseEntityWithHidden.HiddenId.class);
    }

    @Test
    public void testReadModelProviderInterface(){
        var resolver = new GenericInterfaceTypeResolver(ReadModelProviderInterface.class);
        var resolved = resolver.resolveFor(ReadModelProvider.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testReadModelProviderImpl(){
        var resolver = new GenericInterfaceTypeResolver(ReadModelProviderImpl.class);
        var resolved = resolver.resolveFor(ReadModelProvider.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testReadModelProviderImplSub(){
        var resolver = new GenericInterfaceTypeResolver(ReadModelProviderImplSub.class);
        var resolved = resolver.resolveFor(ReadModelProvider.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testReadModelProviderInterfaceBounded(){
        var resolver = new GenericInterfaceTypeResolver(ReadModelProviderInterfaceBounded.class);
        var resolved = resolver.resolveFor(ReadModelProvider.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testDirectReadModelProvider(){
        var resolver = new GenericInterfaceTypeResolver(DirectReadModelProvider.class);
        var resolved = resolver.resolveFor(ReadModelProvider.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testDirectReadModelProviderBounded(){
        var resolver = new GenericInterfaceTypeResolver(DirectReadModelProviderBounded.class);
        var resolved = resolver.resolveFor(ReadModelProvider.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testReadModelProviderSubSub(){
        var resolver = new GenericInterfaceTypeResolver(ReadModelProviderSubSub.class);
        var resolved = resolver.resolveFor(ReadModelProvider.class, 0);
        assertThat(resolved).isEqualTo(MyReadModel.class);
    }

    @Test
    public void testReadModelProviderSubGeneric(){
        var resolver = new GenericInterfaceTypeResolver(ReadModelProviderSubGeneric.class);
        var resolved = resolver.resolveFor(ReadModelProvider.class, 0);
        assertThat(resolved).isEqualTo(ReadModel.class);
    }


}
