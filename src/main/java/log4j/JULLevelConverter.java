/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package log4j;

import org.apache.log4j.Level;

/**
 * Implementations make a decision on how to convert a java.util.logging.Level 
 * instance into a log4j equivalent.
 * 
 * @author psmith
 *
 */
public interface JULLevelConverter {

    /**
     * Convertes a java.util.logging.Level class into an appropriate
     * log4j Level, based on whatever policy you want.
     * 
     * 
     * @param juliLevel
     * @return
     */
    public Level convertJuliLevel(java.util.logging.Level juliLevel);
    
    /**
     * Converts a log4j {@link Level} into a juli {@link java.util.logging.Level}.
     * 
     * @param log4jLevel
     * @return
     */
    public java.util.logging.Level convertLog4jLevel(Level log4jLevel);
}