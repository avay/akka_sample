/**
 * (C) Copyright 2014 Roy Russo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 */


package com.akka.fix1.DbPersistenceService;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author royrusso@gmail.com
 */
public class ProcessorState implements Serializable {
    private final ArrayList<String> userDetails;

    public ProcessorState() {
        this(new ArrayList<String>());
    }

    public ProcessorState(ArrayList<String> events) {
        this.userDetails = events;
    }

    public ProcessorState copy() {
        return new ProcessorState(new ArrayList<String>(userDetails));
    }

    public void update(UserDetail arg0) {
        userDetails.add(arg0.toString());
    }

    @Override
    public String toString() {
        return userDetails.toString();
    }
}
