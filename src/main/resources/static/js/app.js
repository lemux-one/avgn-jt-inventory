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
import {
    RestCollection,
    CollectionComponent,
    FormAddComponent,
    NavComponent
} from './modules/common.js';

var root = document.body;
var baseApiUrl = "/api/";

var roles = new RestCollection('roles', baseApiUrl);
var RoleList = new CollectionComponent(roles);
var RoleForm = new FormAddComponent(roles);

var types = new RestCollection('types', baseApiUrl);
var TypeList = new CollectionComponent(types);
var TypeForm = new FormAddComponent(types);

var containers = new RestCollection('containers', baseApiUrl);
var ContainerList = new CollectionComponent(containers);
var ContainerForm = new FormAddComponent(containers);

var stores = new RestCollection('stores', baseApiUrl);
var StoreList = new CollectionComponent(stores,
    "/sections/of/:store", {store: "name"});
var StoreForm = new FormAddComponent(stores);

var sections = new RestCollection('sections', baseApiUrl);
var SectionList = new CollectionComponent(sections,
    "/products/of/:section", {section: "name"});
var SectionForm = new FormAddComponent(sections);

var products = new RestCollection('products', baseApiUrl);
var ProductList = new CollectionComponent(products);
var ProductForm = new FormAddComponent(products);

var managers = new RestCollection('managers', baseApiUrl);
var ManagerList = new CollectionComponent(managers);
var ManagerForm = new FormAddComponent(managers);

NavComponent.links = [
    {route: roles.route, title: "Roles"},
    {route: types.route, title: "Types"},
    {route: containers.route, title: "Containers"},
    {route: stores.route, title: "Stores"},
    {route: sections.route, title: "Sections"},
    {route: products.route, title: "Products"},
    {route: managers.route, title: "Managers"}
];

m.route(root, "/", {
    "/": {view: () => {return [m(NavComponent), m(StoreList)];}},
    "/roles": {view: () => {return [m(NavComponent), m(RoleList)];}},
    "/roles/add": {view: () => {return [m(NavComponent), m(RoleForm)];}},
    "/types": {view: () => {return [m(NavComponent), m(TypeList)];}},
    "/types/add": {view: () => {return [m(NavComponent), m(TypeForm)];}},
    "/containers": {view: () => {return [m(NavComponent), m(ContainerList)];}},
    "/containers/add": {view: () => {return [m(NavComponent), m(ContainerForm)];}},
    "/stores": {view: () => {return [m(NavComponent), m(StoreList)];}},
    "/stores/add": {view: () => {return [m(NavComponent), m(StoreForm)];}},
    "/sections": {view: () => {return [m(NavComponent), m(SectionList)];}},
    "/sections/of/:store": {view: (vnode) => {
        SectionList.query = {"store.name": vnode.attrs.store};
        return [m(NavComponent), m(SectionList)];
    }},
    "/sections/add": {view: () => {return [m(NavComponent), m(SectionForm)];}},
    "/products": {view: () => {return [m(NavComponent), m(ProductList)];}},
    "/products/of/:section": {view: (vnode) => {
        ProductList.query = {"section.name": vnode.attrs.section};
        return [m(NavComponent), m(ProductList)];
    }},
    "/products/add": {view: () => {return [m(NavComponent), m(ProductForm)];}},
    "/managers": {view: () => {return [m(NavComponent), m(ManagerList)];}},
    "/managers/add": {view: () => {return [m(NavComponent), m(ManagerForm)];}}
});
