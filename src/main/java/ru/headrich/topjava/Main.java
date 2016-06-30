package ru.headrich.topjava;

import ru.headrich.topjava.util.converters.PasswordEncryption;

/**
 * User: gkislin
 * Date: 05.08.2015
 *
 * @link http://caloriesmng.herokuapp.com/
 * @link https://github.com/JavaOPs/topjava
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.format("Hello Topjava Enterprise!");
        String hashp = PasswordEncryption.getEncryptor().getHash("201941monstr");
        System.out.println(PasswordEncryption.getEncryptor().authenticate("201941monstr",hashp));


    }
}
