/*******************************************************************************
 * Copyright 2013 Francesco Cina'
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jporm.validator.jsr303;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ufo
 */
public class Song {

    private Long id;
    private Long lyricId;

    @NotNull(message = "notNull")
    @Size(min = 3, message = "minLenght3")
    private String title;

    @NotNull(message = "notNull")
    @Size(min = 3, message = "minLenght3")
    private String artist;
    // @Size(min = 4, message="minLenght4")
    @Min(value = 1900, message = "minSize1900")
    private Integer year;

    public String getArtist() {
        return this.artist;
    }

    public Long getId() {
        return this.id;
    }

    public Long getLyricId() {
        return this.lyricId;
    }

    public String getTitle() {
        return this.title;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setArtist(final String artist) {
        this.artist = artist;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setLyricId(final Long lyricId) {
        this.lyricId = lyricId;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

}
