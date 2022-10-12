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
import one.lemux.avgnjtinventory.models.Role;
import one.lemux.avgnjtinventory.models.Store;
import one.lemux.avgnjtinventory.models.Type;
import one.lemux.avgnjtinventory.repositories.ContainerRepository;
import one.lemux.avgnjtinventory.repositories.RoleRepository;
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

    @Autowired
    public DatabaseLoader(
            ContainerRepository containers, 
            TypeRepository types, 
            RoleRepository roles,
            StoreRepository stores) {
        this.containers = containers;
        this.types = types;
        this.roles = roles;
        this.stores = stores;
    }

    @Override
    public void run(String... args) throws Exception {
        containers.save(new Container("Cartón"));
        containers.save(new Container("Plástico"));
        containers.save(new Container("Cristal"));
        containers.save(new Container("Nylon"));

        types.save(new Type("Electrodoméstico"));
        types.save(new Type("Cárnico"));
        types.save(new Type("Ropa"));
        types.save(new Type("Aseo"));

        roles.save(new Role("Administrador"));
        roles.save(new Role("Operario"));
        
        stores.save(new Store("ECASA-1"));
    }

}
