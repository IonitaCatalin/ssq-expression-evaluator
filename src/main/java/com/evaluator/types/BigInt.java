package com.evaluator.types;

import com.evaluator.types.exceptions.DivisionByZeroException;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.types.exceptions.NegativeValueException;

import java.util.Arrays;
import java.util.Objects;

public class BigInt implements Comparable<BigInt>{
    private final int numberOfDigits;
    private final int[] value;
    private final static int MAX_NUMBER_OF_DIGITS = 100_000;

    public BigInt(){
        numberOfDigits = 0;
        value = new int[0];
    }

    public BigInt(String val) throws MaximumNumberOfDecimalExceededException, InvalidNumberFormatException {

        if(val.length() > MAX_NUMBER_OF_DIGITS){
            throw new MaximumNumberOfDecimalExceededException();
        }

        //Alocate the memmory
        numberOfDigits = val.length();
        value = new int[numberOfDigits];

        for(int i=0; i<val.length(); i++){
            int digit = Character.getNumericValue(val.charAt(i));
            if(digit < 0 || digit >9){
                throw new InvalidNumberFormatException();
            }

            value[val.length() - i - 1] = digit;
        }
    }

    public BigInt(int[] value){
        this.value = value;
        this.numberOfDigits = value.length;
    }

    public BigInt(BigInt b){
        this.value = b.getValue();
        this.numberOfDigits = b.getNumberOfDigits();
    }

    public BigInt(int i){
        int length = String.valueOf(i).length();
        this.value = new int[length];
        this.numberOfDigits = length;

        int counter = 0;

        while(i > 0){
            this.value[counter] = i % 10;
            i = i / 10;
            counter++;
        }
    }

    public BigInt add(BigInt n) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {

        int minDigits = Math.min(this.numberOfDigits, n.getNumberOfDigits());
        int maxDigits = Math.max(this.numberOfDigits, n.getNumberOfDigits());

        int carry = 0;

        String value = "";

        for(int i = 0; i < minDigits; i++){
            int digit = this.value[i] + n.value[i] + carry;
            carry = 0;

            if(digit > 9){
                carry = 1;
                digit = this.value[i] + n.value[i] - 10 + (i != 0 ? carry: 0);
            }

            value = String.valueOf((digit)).concat(value);
        }

        if(this.numberOfDigits > n.numberOfDigits) {
            for(int i = minDigits; i < maxDigits; i++){
                int digit = this.value[i] + carry;
                carry = 0;

                if(digit > 9){
                    carry = 1;
                    digit = this.value[i] - 10 + carry;
                }

                value = String.valueOf((digit)).concat(value);
            }
        } else {
            for(int i = minDigits; i< maxDigits; i++){
                int digit = n.getValue()[i] + carry;
                carry = 0;

                if(digit > 9){
                    carry = 1;
                    digit = n.value[i] - 10 + carry;
                }

                value = String.valueOf((digit)).concat(value);
            }
        }

        if(carry > 0){
            value = String.valueOf((carry)).concat(value);
        }

        return new BigInt(value);

    }

    public BigInt subtract(BigInt n) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, NegativeValueException {

        if(this.compareTo(n) < 0){
            throw new NegativeValueException();
        }

        int n1 = this.getNumberOfDigits();
        int n2 = n.getNumberOfDigits();
        String str = "";

        int carry = 0;

        for (int i = 0; i < n2; i++) {
            int sub = (this.value[i] - n.getValue()[i] - carry);

            if (sub < 0) {
                sub = sub + 10;
                carry = 1;
            }
            else
                carry = 0;

            str = (String.valueOf(sub)).concat(str);
        }

        for (int i = n2; i < n1; i++) {
            int sub = (this.value[i] - carry);
            if (sub < 0) {
                sub = sub + 10;
                carry = 1;
            }
            else
                carry = 0;


            str = (String.valueOf(sub)).concat(str);
        }

        while(str.startsWith("0")){
            str = str.substring(1);
        }

        return new BigInt(str);
    }

    public BigInt multiply(BigInt n) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        int len1 = this.numberOfDigits;
        int len2 = n.getNumberOfDigits();

        if (len1 == 0 || len2 == 0) {
            return new BigInt("0");
        }


        int[] result = new int[len1 + len2];
        int i_n1 = 0;
        int i_n2 = 0;

        for (int i = 0; i < len1; i++)
        {
            int carry = 0;
            int n1 = this.value[i];
            i_n2 = 0;

            for (int j = 0; j < len2; j++)
            {
                int n2 = n.getValue()[j];
                int sum = n1 * n2 + result[i_n1 + i_n2] + carry;

                carry = sum / 10;
                result[i_n1 + i_n2] = sum % 10;

                i_n2++;
            }

            if (carry > 0) {
                result[i_n1 + i_n2] += carry;
            }

            i_n1++;
        }

        // ignore '0's from the right
        int i = result.length - 1;
        while (i >= 0 && result[i] == 0) {
            i--;
        }

        if (i == -1) {
            return new BigInt("0");
        }

        String s = "";

        while (i >= 0) {
            s = s.concat(String.valueOf(result[i--]));
        }

        return new BigInt(s);
    }

    public BigInt divide(int divisor) throws DivisionByZeroException, InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {

        if(divisor == 0){
            throw new DivisionByZeroException();
        }

        StringBuilder result = new StringBuilder();
        int carry = 0;

        for (int i = this.numberOfDigits - 1; i >= 0; i--) {
            int x = carry * 10 + this.value[i];
            result.append(x / divisor);
            carry = x % divisor;
        }

        int i;
        for (i = 0; i < result.length(); i++) {
            if (result.charAt(i) != '0') {
                break;
            }
        }

        return new BigInt(result.substring(i));
    }

    public BigInt divide(BigInt divisor) throws DivisionByZeroException, InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, NegativeValueException {
        BigInt b = new BigInt(this);

        if(divisor.compareTo(new BigInt(0)) == 0) {
            throw new DivisionByZeroException();
        }

        BigInt count = new BigInt(0);

        while(b.compareTo(divisor) >= 0) {
            b = new BigInt(b.subtract(divisor));
            count = count.add(new BigInt(1));
        }

        return count;
    }

    public BigInt pow(int power) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        if(power == 0){
            return new BigInt("1");
        }

        BigInt b = new BigInt(this);

        for(int i = 1; i < power; i++){
            b = new BigInt(b.multiply(this));
        }

        return b;
    }

    public BigInt pow(BigInt power) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, NegativeValueException {
        if(power.compareTo(new BigInt(0)) == 0 ) {
            return new BigInt(1);
        }

        BigInt b = new BigInt(this);

        while(!(power.compareTo(new BigInt(1)) == 0)) {
            b = new BigInt(b.multiply(this));
            power = power.subtract(new BigInt(1));
        }

        return b;
    }

    public BigInt sqrt(int root) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        for(int i = 1; i < Integer.MAX_VALUE; i++){
            BigInt b = new BigInt(i);
            if(b.pow(root).compareTo(this) > 0){
                return new BigInt(i-1);
            }
        }

        return null;
    }

    public BigInt sqrt(BigInt root) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, NegativeValueException {

        for(int i = 1; i < Integer.MAX_VALUE; i++){
            BigInt b = new BigInt(i);
            if(b.pow(root).compareTo(this) > 0){
                return new BigInt(i-1);
            }
        }

        return null;

    }


    public int[] getValue() {
        return value;
    }

    public int getNumberOfDigits() {
        return numberOfDigits;
    }

    public String convertToString(){
        String s = "";

        for (int j : value) {
            s = String.valueOf(j).concat(s);
        }

        return s;
    }

    @Override
    public int compareTo(BigInt b){
        if(this.numberOfDigits < b.numberOfDigits){
            return -1;
        }else if(this.numberOfDigits > b.numberOfDigits){
            return 1;
        }

        //by this point it means they have an equal number of digits
        for(int i = this.getNumberOfDigits() - 1; i>=0; i--){
            if(this.value[i] < b.value[i]){
                return -1;
            }else if(this.value[i] > b.value[i]){
                return 1;
            }
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BigInt BigInt = (BigInt) o;
        return numberOfDigits == BigInt.numberOfDigits && Arrays.equals(value, BigInt.value);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(numberOfDigits);
        result = 31 * result + Arrays.hashCode(value);
        return result;
    }

    @Override
    public String toString() {
        return this.convertToString();
    }
}
