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
package one.lemux.avgnjtinventory.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author lemux
 */
@Entity
@Data
@RequiredArgsConstructor @NoArgsConstructor
public class Product implements Serializable {
    
    private @Id @GeneratedValue Long id;
    private @NonNull String name;
    // TODO define how to handle "size" field
    private String color;
    private @NonNull Double unit_price;
    private Boolean isFragile;
    private @ManyToOne Container container;
    private Long lot;
    private @NonNull Long stock;
    private @ManyToOne Section section;
    
}
