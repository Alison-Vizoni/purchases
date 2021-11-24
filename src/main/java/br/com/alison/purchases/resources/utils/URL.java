package br.com.alison.purchases.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URL {

    public static String decodeParam(String string){
        return URLDecoder.decode(string, StandardCharsets.UTF_8);
    }

    public static List<Long> decodeLongList(String string){
        return Arrays.stream(string.split(",")).map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
