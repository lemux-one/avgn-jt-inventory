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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;

/**
 *
 * @author lemux
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class ApiDocumentation {

    private MockMvc mvc;

    @BeforeEach
    public void setUp(
            WebApplicationContext appContext,
            RestDocumentationContextProvider restDocContext) {
        this.mvc = MockMvcBuilders.webAppContextSetup(appContext)
                .apply(documentationConfiguration(restDocContext))
                .build();
    }

    @Test
    public void index() throws Exception {
        this.mvc.perform(get("/api/").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(document("index", links(halLinks(),
                        linkWithRel("types"),
                        linkWithRel("stores"),
                        linkWithRel("roles"),
                        linkWithRel("containers"),
                        linkWithRel("sections"),
                        linkWithRel("managers"),
                        linkWithRel("products"),
                        linkWithRel("profile")
                )));
    }
}
