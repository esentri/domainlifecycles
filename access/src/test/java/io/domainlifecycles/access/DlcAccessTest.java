package io.domainlifecycles.access;

import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.access.object.DefaultDomainObjectAccessFactory;
import io.domainlifecycles.access.object.DefaultEnumFactory;
import io.domainlifecycles.access.object.DefaultIdentityFactory;
import javax.swing.plaf.basic.BasicListUI.FocusHandler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DlcAccessTest {

    @Test
    void testCustomizeOk() {

        DefaultClassProvider provider = new DefaultClassProvider();
        DlcAccess.customize(provider, new DefaultEnumFactory(provider),
            new DefaultIdentityFactory(provider), new DefaultDomainObjectAccessFactory());
    }

    @Test
    void testGetClassProviderOk() {

        DefaultClassProvider defaultClassProviderMock = mock(DefaultClassProvider.class);
        Object mock = mock();
        //Mockito.when(defaultClassProviderMock.getClassForName(Mockito.eq("someClass"))).thenReturn(mock);

        Class<?> aClass = DlcAccess.getClassForName("someClass");

        Assertions.assertThat(aClass).isNotNull();
        Assertions.assertThat(aClass).isInstanceOf(Class.class);

        verify(defaultClassProviderMock, times(1)).getClassForName(eq("someClass"));
    }

    @Test
    void testNewEnumInstanceOk() {

        DefaultEnumFactory defaultEnumFactoryMock = mock(DefaultEnumFactory.class);
        Mockito.when(defaultEnumFactoryMock.newInstance(Mockito.eq("someValue"), Mockito.eq("enumType"))).thenReturn(mock(Enum.class));

        Enum<?> someEnum = DlcAccess.newEnumInstance("someValue", "enumType");

        Assertions.assertThat(someEnum).isNotNull();
        Assertions.assertThat(someEnum).isInstanceOf(Enum.class);

        verify(defaultEnumFactoryMock, times(1)).newInstance(Mockito.eq("someValue"), Mockito.eq("enumType"));
    }

}
