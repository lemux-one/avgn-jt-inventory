package one.lemux.avgnjtinventory;

import one.lemux.avgnjtinventory.repositories.ContainerRepository;
import one.lemux.avgnjtinventory.repositories.ManagerRepository;
import one.lemux.avgnjtinventory.repositories.ProductRepository;
import one.lemux.avgnjtinventory.repositories.RoleRepository;
import one.lemux.avgnjtinventory.repositories.SectionRepository;
import one.lemux.avgnjtinventory.repositories.StoreRepository;
import one.lemux.avgnjtinventory.repositories.TypeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AvgnJtInventoryApplicationTests {

    @Autowired private ContainerRepository containers;
    @Autowired private ManagerRepository managers;
    @Autowired private ProductRepository products;
    @Autowired private RoleRepository roles;
    @Autowired private SectionRepository sections;
    @Autowired private StoreRepository stores;
    @Autowired private TypeRepository types;
    
    @Test
    public void contextLoads() {
        Assertions.assertThat(containers).isNotNull();
        Assertions.assertThat(managers).isNotNull();
        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(roles).isNotNull();
        Assertions.assertThat(sections).isNotNull();
        Assertions.assertThat(stores).isNotNull();
        Assertions.assertThat(types).isNotNull();
    }

}
