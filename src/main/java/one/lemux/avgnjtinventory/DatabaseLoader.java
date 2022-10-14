/*
 * Copyright (C) 2022 lemux
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package one.lemux.avgnjtinventory;

import one.lemux.avgnjtinventory.models.Container;
import one.lemux.avgnjtinventory.models.Manager;
import one.lemux.avgnjtinventory.models.Product;
import one.lemux.avgnjtinventory.models.Role;
import one.lemux.avgnjtinventory.models.Section;
import one.lemux.avgnjtinventory.models.Store;
import one.lemux.avgnjtinventory.models.Type;
import one.lemux.avgnjtinventory.repositories.ContainerRepository;
import one.lemux.avgnjtinventory.repositories.ManagerRepository;
import one.lemux.avgnjtinventory.repositories.ProductRepository;
import one.lemux.avgnjtinventory.repositories.RoleRepository;
import one.lemux.avgnjtinventory.repositories.SectionRepository;
import one.lemux.avgnjtinventory.repositories.StoreRepository;
import one.lemux.avgnjtinventory.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author lemux
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final ContainerRepository containers;
    private final TypeRepository types;
    private final RoleRepository roles;
    private final StoreRepository stores;
    private final SectionRepository sections;
    private final ProductRepository products;
    private final ManagerRepository managers;

    @Autowired
    public DatabaseLoader(
            ContainerRepository containers, 
            TypeRepository types, 
            RoleRepository roles,
            StoreRepository stores,
            SectionRepository sections,
            ProductRepository products,
            ManagerRepository managers) {
        this.containers = containers;
        this.types = types;
        this.roles = roles;
        this.stores = stores;
        this.sections = sections;
        this.products = products;
        this.managers = managers;
    }

    @Override
    public void run(String... args) throws Exception {
        containers.save(new Container("Cartón"));
        containers.save(new Container("Plástico"));
        containers.save(new Container("Cristal"));
        var nylon = containers.save(new Container("Nylon"));

        types.save(new Type("Electrodoméstico"));
        types.save(new Type("Cárnico"));
        var typeClothing = types.save(new Type("Ropa"));
        var typeCleaning = types.save(new Type("Aseo"));

        var rolAdmin = roles.save(new Role("Administrador"));
        var rolOpe = roles.save(new Role("Operario"));
        
        var store1 = stores.save(new Store("Almacen1"));
        var store2 = stores.save(new Store("Almacen2"));
        
        managers.save(new Manager("admin", rolAdmin, store1));
        managers.save(new Manager("operator", rolOpe, store1));
        
        var children = sections.save(new Section(
                "Ropa Infantil",
                55.50,
                store1,
                typeClothing));
        sections.save(new Section(
                "Ropa Femenina", 
                86.50,
                store1,
                typeClothing));
        sections.save(new Section(
                "Productos de tocador", 
                86.50,
                store2,
                typeCleaning));
        
        products.save(new Product("Blusa Veraniega",
                "Rojo", 5.80, Boolean.FALSE,
                nylon, 7896546L, 500L,
                children));
    }

}
