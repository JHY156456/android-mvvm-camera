package com.example.mvvmappapplication.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by kck on 2016-07-04.
 */
public class WebDESUtil {
    /**
     * DES 키 생성
     * @param bytekey
     * @return
     */
    private static SecretKey getKey(byte[] bytekey) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        DESKeySpec desKeySpec = new DESKeySpec(bytekey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(desKeySpec);

    }//getKey




    /**
     * 블럭단위 DES 암호화
     * @param data
     * @param key
     * @return
     */
    private static byte[] enc(byte[] data, byte[] key) throws Exception
    {

        if(data == null || data.length == 0) return null;

        // String instance = (key.length == 24) ? "DESede/ECB/PKCS5Padding"
        //		: "DES/ECB/PKCS5Padding";

        String instance = "DES/ECB/NoPadding";

        //Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        Cipher cipher = Cipher.getInstance(instance);

        cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
        byte[] outputBytes = cipher.doFinal(data);
        return outputBytes;

    }//enc




    /**
     * 블럭단위 DES 복호화
     * @param data
     * @param key
     */
    private static byte[] dec(byte[] data, byte[] key) throws Exception
    {

        if(data == null || data.length == 0) return null;
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, getKey(key));
        return cipher.doFinal(data);

    }//dec




    /**
     * 패딩처리
     * @param pData
     * @return
     */
    private static byte[] padding(byte[] pData)
    {
        int iMod;
        int nData = pData.length;

        iMod = nData % 8;

        byte[] rtn = null;

        if (iMod != 0)
        {
            byte[] attach = new byte[8 - iMod];
            for(int i=0 ; i<attach.length ; ++i) attach[i] = (byte)0x80;
            rtn = merge(pData, attach);
        }
        else rtn = pData;

        return rtn;
    }//padding

    /**
     * 2개의 배열을 합친 새로운 배열 리턴
     * @param base
     * @param attach
     * @return
     */
    private static byte[] merge(byte[] base, byte[] attach)
    {
        if(base == null && attach != null) return attach;
        if(attach == null && base != null) return base;

        int baseCnt = base.length;
        int attachCnt = attach.length;

        byte[] rtn = new byte[baseCnt + attachCnt];

        int j=0;

        for(int i=0 ; i<baseCnt ; ++i, ++j)
        {
            rtn[i] = base[i];
        }


        System.arraycopy(attach, 0, rtn, j, attachCnt);

        return rtn;

    }//merge


    /**
     * 언패딩처리
     * @param data
     * @return
     */
    private static byte[] unPadding(byte[] data)
    {

        int len = data.length;
        int delCnt = 0;

        while(data[--len] == (byte)0x80) ++delCnt;
        ++len;

        return subByte(data, 0, len);

    }//unPadding


    public static byte[] subByte(byte[] base, int pos1, int pos2)
    {
        int baseCnt = base.length;
        if(baseCnt < pos1 + pos2) return null;
        byte[] rtn = new byte[pos2];
        System.arraycopy(base, pos1 + 0, rtn, 0, pos2);
        return rtn;
    }




    /**
     * Hex 형식 로그 출력을 위한 함수
     * @param b
     */
    private static void printHex(byte[] b)
    {
        System.out.print("hex : [");
        for(int i=0 ; i<b.length ; ++i)
        {
            String hex = Integer.toHexString(b[i]).toUpperCase();
            if(hex.length() > 2) System.out.print(hex.substring(6));
            else System.out.print(hex);
        }
        System.out.print("]");
        System.out.println();

    }//printHex






    /**
     * KTF DES 암호화 노출 함수
     * @param padding
     * @param data
     * @param key
     * @param log
     * @return
     */
    public static byte[] encrypt(boolean padding, byte[] data, byte[] key, boolean log) throws Exception
    {

        if(log)
        {
            System.out.println("");
            System.out.println("<========== DES 데이타 암호화 시작");
            System.out.println("* org data *");
            System.out.println("org : ["+new String(data)+"]");
            printHex(data);
        }
        byte[] plaintext = null;
        if(padding) plaintext = padding(data);
        else plaintext = data;


        int size = plaintext.length;
        byte[] ciphertext = new byte[size];
        byte[] p = new byte[8];


        System.arraycopy(plaintext, 0, p, 0, 8);
        p = enc(p, key);

        System.arraycopy(p, 0, ciphertext, 0, 8);



        for(int i = 1 ; i < size/8  ; i++)
        {
            for(int j=0 ; j<8 ; j++)
            {
                p[j] ^= plaintext[j+ (i * 8)];
                //p[j] = plaintext[j+ (i * 8)];
            }
            p = enc(p, key); //32
            System.arraycopy(p, 0, ciphertext, (i * 8), 8);
        }


        if(log)
        {
            System.out.println("* des enc data *");
            printHex(ciphertext);
            System.out.println("* DES 데이타 암호화 끝 ==========>");
            System.out.println("");
        }

        return ciphertext;

    }//encrypt




    /**
     * KTF DES 복호화 노출 함수
     * @param padding
     * @param data
     * @param key
     * @param log
     * @return
     */
    public static byte[] decrypt(boolean padding, byte[] data, byte[] key, boolean log) throws Exception
    {

        if(log)
        {
            System.out.println("");
            System.out.println("<========== KTF DES 데이타 복호화 시작");
            System.out.println("* org data *");
            System.out.println("org : ["+new String(data)+"]");
            printHex(data);
        }

        int size = data.length;
        byte[] p = new byte[8];
        byte[] plaintext = new byte[size];


        for(size -= 8 ;  size > 0 ; size -=8)
        {
            System.arraycopy(data, size, p, 0, 8);
            p = dec(p, key);
            for(int j = 0; j < 8; j++) plaintext[j + size] = (byte)(p[j] ^ data[size - 8 + j]);
        }


        System.arraycopy(data, 0, p, 0, 8);
        p = dec(p, key);
        System.arraycopy(p, 0, plaintext, 0, 8);


        byte[] rtn = null;
        if(padding) rtn = unPadding(plaintext);
        else rtn = plaintext;

        if(log)
        {
            System.out.println("* des dec data *");
            printHex(rtn);
            System.out.println("KTF DES 데이타 복호화 끝 ==========>");
            System.out.println("");
        }

        return rtn;

    }//decrypt





    /**
     * 테스트를 위한 main
     * @param args
     */
    public static void main(String args[])
    {

        String str = "210000001|WPTJD001|7105271041821";
        String keyData = "33344477";
        boolean isDebug = false;

        byte[] desKey = keyData.getBytes( );


        System.out.println("원본");
        System.out.println(str);

        System.out.println("암호화");
        try
        {
            byte[] ciphertext = WebDESUtil.encrypt(true, str.getBytes(), desKey, isDebug);

            String ii = hexToString(ciphertext);
            System.out.println(ii);
    		 /*
    		 for(int i=0 ; i<ciphertext.length ; ++i)
             {
             	String hex = Integer.toHexString(ciphertext[i]).toString();
             	if(hex.length() > 2) System.out.print(hex.substring(6));
             	else System.out.print(hex);
             }*/


            //String iiii = "ee92abe7a4773ffc105b620d48fea81f57db7b910f861dfe36425d0107f5ee4d61a225be89899506082badee7d829d";
            System.out.println("\n받은스트링");
            System.out.println(ii);
            byte[] ciphertext2=stringToHex(ii);
            byte[] aa = decrypt(true, ciphertext2, desKey, isDebug);
            System.out.println("\n복호화");
            System.out.println(new String(aa, "EUC-KR"));



    		 /*
    		 byte[] aa = decrypt(true, ciphertext, desKey, isDebug);
    		 System.out.println("\n복호화");
             System.out.println(new String(aa, "EUC-KR"));
    		 */
        } catch(Exception e)
        {
            e.printStackTrace();
        }





    	/*
    	byte[] desKeyData = {0x12, 0x34, 0x56, 0x78, (byte)0x90, (byte)0xab, (byte)0xcd, (byte)0xef};

        try
        {
        	for(int i= 0 ; i<desKeyData.length ; ++i) System.out.print(desKeyData[i] + ", ");
        	System.out.println("");

        	String str = "English, 한글, 中國語등 출력 테스트 OK?";
        	byte[] ciphertext = encrypt(true, str.getBytes(), desKeyData, true);


            for(int i=0 ; i<ciphertext.length ; ++i)
            {
            	String hex = Integer.toHexString(ciphertext[i]).toString().toUpperCase();
            	if(hex.length() > 2) System.out.print(hex.substring(6));
            	else System.out.print(hex);
            }
            System.out.println();

            byte[] aa = decrypt(true, ciphertext, desKeyData, true);



            System.out.println("복호화");
            System.out.println(new String(aa, "EUC-KR"));

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        */

    }//main

    public static String hexToString(byte[ ] x )
    {

        int byteLen = x.length;

        StringBuffer tempHexStr = new StringBuffer( );

        for( int i = 0 ; i < byteLen ; i++ )
        {
            int unsigned = x[ i ] & 0xff;

            if( unsigned < 16 ) // 15까지는 한자리가 나오므로
                tempHexStr.append( "0" );
            tempHexStr.append( Integer.toHexString( unsigned ).toUpperCase( ) );
        }

        return tempHexStr.toString( );
    }


    public static byte[ ] stringToHex( String input )
    {

        if( input == null ) return new byte[ 0 ];

        int len = input.length( );
        char[ ] hex = input.toCharArray( );
        byte[ ] buf = new byte[ len / 2 ];

        for( int pos = 0 ; pos < len / 2 ; pos++ )
            buf[ pos ] = ( byte ) ( ( ( toDataNibble( hex[ 2 * pos ] ) << 4 ) & 0xF0 ) | ( toDataNibble( hex[ 2 * pos + 1 ] ) & 0x0F ) );

        return buf;
    }

    public static byte toDataNibble( char c )
    {

        if( ( '0' <= c ) && ( c <= '9' ) ) return ( byte ) ( ( byte ) c - ( byte ) '0' );
        else if( ( 'a' <= c ) && ( c <= 'f' ) ) return ( byte ) ( ( byte ) c - ( byte ) 'a' + 10 );
        else if( ( 'A' <= c ) && ( c <= 'F' ) ) return ( byte ) ( ( byte ) c - ( byte ) 'A' + 10 );
        else return -1;
    }

}//DES