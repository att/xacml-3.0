/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MainUtils {
    
    private MainUtils() {
        // Empty
    }

    public static Collection<String> santizeArguments(String[] args) {
        List<String> sanitized = new LinkedList<>();

        //
        // Check that there are too many
        //
        if (args.length > 10) {
            return sanitized;
        }
        for (String arg : args) {
            if (arg.length() > 2048) {
                continue;
            }
            sanitized.add(arg);
        }
        
        return sanitized;
    }
    
}
