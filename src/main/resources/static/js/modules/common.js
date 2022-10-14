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
var loadJSON = (url, onDone, onError) => {
    m.request({
        method: "GET",
        url: url,
        headers: {
            "Accept": "application/hal+json"
        }
    }).then(function(resp) {
        onDone(resp);
    }).catch(function(err) {
        onError(err);
    });
};

class RestItem {
    constructor(collectionItem, parent) {
        this.entity = collectionItem;
        this.parent = parent;
        this.relations = {};
        this.etag = null;
    }
    load() {
        const self = this;
        m.request({
            method: "GET",
            url: self.entity._links.self.href,
            headers: {
                "Accept": "application/hal+json"
            },
            extract: (xhr) => {return xhr;}
        }).then(function(resp) {
            const etag = resp.getResponseHeader("etag");
            //console.log(resp.getAllResponseHeaders());
            self.entity = JSON.parse(resp.responseText);
            if (etag) {
                self.etag = parseInt(etag.replaceAll('"', ''));
            }
            self.parent.attrs.forEach((attr) => {
                if (!self.entity[attr] && self.entity._links[attr]) {
                    loadJSON(self.entity._links[attr].href, (data) => {
                        self.relations[attr] = data;
                    }, (err) => {
                        console.log(err.code);
                    });
                }
            });
            //console.log(self);
        }).catch(function(err) {
            console.log(err.code);
        });
    }
}

class RestCollection {
    constructor(rel, apiRoot, route = null, query = {}) {
        this.apiRoot = apiRoot;
        this.rel = rel;
        this.entity = {};
        this.schema = {};
        this.items = [];
        this.fullItems = [];
        this.attrs = [];
        if (!route) {
            this.route = "/"+this.rel;
        } else {
            this.route = route;
        }
        this.query = query;
    }
    load(query = {}) {
        const self = this;
        m.request({
            method: "GET",
            url: self.apiRoot + self.rel,
            headers: {
                "Accept": "application/hal+json"
            },
            params: query
        }).then(function(resp) {
            self.entity = resp;
            self.items = resp._embedded[self.rel];
            self.__loadProfile();
            self.__loadFullItems();
        }).catch(function(err) {
            console.log(err.code);
        });
    }
    __loadFullItems() {
        const self = this;
        this.fullItems = [];
        this.items.forEach((item) => {
            const fullItem = new RestItem(item, self);
            fullItem.load();
            self.fullItems.push(fullItem);
        });
    }
    __loadProfile() {
        const self = this;
        m.request({
            method: "GET",
            url: self.entity._links.profile.href,
            headers: {
                "Accept": "application/schema+json"
            }
        }).then(function(resp) {
            self.schema = resp;
            self.attrs = [];
            for (const prop in self.schema.properties) {
                self.attrs.push(prop);
            }
        }).catch(function(err) {
            console.log(err.code);
        });
    }
    remove(item) {
        const self = this;
        m.request({
            method: "DELETE",
            url: item._links.self.href,
            headers: {
                "Accept": "application/hal+json"
            }
        }).then(function(resp) {
            console.log(resp);
            self.load();
            m.redraw();
        }).catch(function(err) {
            console.log(err.code);
        });
    }
    add(data) {
        const self = this;
        m.request({
            method: "POST",
            url: self.entity._links.self.href,
            headers: {
                "Accept": "application/hal+json"
            },
            body: data
        }).then(function(resp) {
            console.log(resp);
            window.location.href = self.route;
        }).catch(function(err) {
            console.log(err.code);
        });
    }
};

class CollectionComponent {
    constructor(restCollection, detailsRoute, detailsParams = {}) {
        this.collection = restCollection;
        this.detailsRoute = detailsRoute;
        this.detailsParams = detailsParams;
        this.query = {};
    }
    oninit() {
        this.collection.load(this.query);
    }
    view() {
        const self = this;
        
        // create the headers dynamically
        const ths = [];
        ths.push(m("th", "Actions"));
        ths.push(self.collection.attrs.map((attr) => {
            return m("th", self.collection.schema.properties[attr].title);
        }));
        
        // fill the table
        const table = m("table.one", [
            m("tr", ths),
            self.collection.fullItems.map((fullItem) => {
                const item = fullItem.entity;
                const tds = [];
                const actions = [
                    m("button.error", {title: "Delete element", onclick: () => {
                        self.collection.remove(item);
                    }}, "x"),
                    m("button.warning", {title: "Update element", onclick: () => {
                        //TODO implement this
                    }}, "#")
                ];
                if (self.detailsRoute) {
                    actions.push(m("button", {onclick: () => {
                        const params = {};
                        Object.keys(self.detailsParams).forEach((key) => {
                            params[key] = item[self.detailsParams[key]];
                        });
                        m.route.set(self.detailsRoute, params);
                    }, title: "Inspect elements"}, ">"));
                }
                tds.push(m("td", actions));
                self.collection.attrs.forEach((attr) => {
                    if (attr in item) {
                        if ("boolean" === self.collection.schema.properties[attr].type) {
                            tds.push(m("td", m("p", item[attr].toString())));
                        } else {
                            tds.push(m("td", m("p", item[attr])));
                        }
                    } else if (attr in fullItem.relations) {
                        //tds.push(m("td", m("a", {href: item._links[attr].href}, "Link")));
                        tds.push(m("td", m("p", fullItem.relations[attr].name)));
                    }
                });
                return m("tr", tds);
            })
        ]);
        
        // enable pagination support if applicable
        let pagingComponent = [];
        if (self.collection.entity["page"]) {
            const page = self.collection.entity["page"];
            pagingComponent = m("footter.no-border", [
                //m("label", "Size: " + page.size)
            ]);
        }
        
        // layout the contents
        return m(".content", [
            m('article.card.no-border', [
                m("header.no-border", [
                    m("h3", self.collection.schema.title + " List"),
                    m("a.button.success", {title: "Add element", href: "#!/" + self.collection.rel + "/add"}, "+")
                    //m("a.button.warning", {href: "#"}, "Back")
                ]),
                table,
                pagingComponent
            ])
        ]);
    }
};

class FormAddComponent {
    constructor(restCollection) {
        this.collection = restCollection;
    }
    oninit() {
        this.collection.load();
    }
    view() {
        const self = this;
        const inputs = [];
        self.collection.attrs.forEach((attr) => {
            const prop = self.collection.schema.properties[attr];
            inputs.push(m("p", [
                m("label.stack", prop.title),
                m("input.stack", {type: "input", name: attr, placeholder: prop.title})
            ]));
        });
        return m(".content", [
            m("h3", "New " + self.collection.schema.title),
            m("form", [
                inputs,
                m("button.success", {type: "button", title: "Submit changes", onclick: (evt) => {
                    const elements = evt.target.form.elements;
                    const data = {};
                    let name = "";
                    for (let i = 0; i < elements.length; i++) {
                        if (elements[i].attributes.name) {
                            name = elements[i].attributes.name.value;
                            data[name] = elements[i].value;
                        }
                    }
                    //console.log(data);
                    self.collection.add(data);
                }}, "Create"),
                m("a.button.error", {title: "Discard any changes", href: self.collection.route}, "Discard")
            ])
        ]);
    }
};

class FormUpdateComponent {
    constructor(restItem) {
        this.item = restItem;
    }
    oninit() {}
};

var NavComponent = {
    links: [],
    home: "/",
    view: () => {
        const links = NavComponent.links.map((link) => {
            const attributes = {href: link.route, className: "pseudo button"};
            if (m.route.get() === link.route) {
                attributes["disabled"] = "disabled";
            }
            return m(m.route.Link, attributes, link.title);
        });
        return m("nav", [
            m("a.brand", {href: NavComponent.home}, [
                m("img.brand-icon", {
                    src: "/img/check.svg", 
                    alt: "Check Icon"
                }),
                m("span", "Inventory")
            ]),
            
            //responsive part
            m("input.show[type=checkbox]", {id: "menu"}),
            m("label.burger.pseudo.button[for=menu]", "Menu"),
            
            m(".menu", links)
        ]);
    }
};

export { RestCollection, CollectionComponent, FormAddComponent, NavComponent };