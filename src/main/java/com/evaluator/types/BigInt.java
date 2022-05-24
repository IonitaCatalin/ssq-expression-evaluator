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

        assert this.isValid();
    }

    public BigInt(String val) throws MaximumNumberOfDecimalExceededException, InvalidNumberFormatException {
        assert val != null;

        if(val.length() > MAX_NUMBER_OF_DIGITS){
            throw new MaximumNumberOfDecimalExceededException();
        }

        while(val.startsWith("0")){
            val = val.substring(1);
        }
        if(val.equals("")){
            val = "0";
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

        assert this.isValid();
        assert this.value.length == val.replaceFirst("^0+(?!$)", "").length();
        assert this.numberOfDigits == val.replaceFirst("^0+(?!$)", "").length();
    }

    public BigInt(int[] value){
        this.value = value;
        this.numberOfDigits = value.length;

        assert this.isValid();
    }

    public BigInt(BigInt b){
        assert b.isValid();

        this.value = b.getValue();
        this.numberOfDigits = b.getNumberOfDigits();

        assert this.isValid();
    }

    public BigInt(int i){
        int length = String.valueOf(i).length();
        int temp = i;
        this.value = new int[length];
        this.numberOfDigits = length;

        int counter = 0;

        while(i > 0){
            assert i > 0;
            assert counter + String.valueOf(i).length() == length;

            this.value[counter] = i % 10;
            i = i / 10;
            counter++;
        }

        assert this.isValid();
        assert Objects.equals(this.convertToString(), String.valueOf(temp));
    }

    public BigInt add(BigInt n) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        assert this.isValid();
        assert n.isValid();

        int minDigits = Math.min(this.numberOfDigits, n.getNumberOfDigits());
        int maxDigits = Math.max(this.numberOfDigits, n.getNumberOfDigits());

        int carry = 0;

        String value = "";

        for(int i = 0; i < minDigits; i++){
            assert i < minDigits;
            assert value.length() == i;

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
                assert i >= minDigits;
                assert i < maxDigits;
                assert value.length() == i;

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
                assert i >= minDigits;
                assert i < maxDigits;
                assert value.length() == i;
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

        BigInt b = new BigInt(value);

        assert b.isValid();
        assert b.compareTo(this) >= 0;
        assert b.compareTo(n) >= 0;

        return b;

    }

    public BigInt subtract(BigInt n) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, NegativeValueException {
        assert this.isValid();
        assert n.isValid();

        if(this.compareTo(n) < 0){
            throw new NegativeValueException();
        }

        int n1 = this.getNumberOfDigits();
        int n2 = n.getNumberOfDigits();
        String str = "";

        int carry = 0;

        for (int i = 0; i < n2; i++) {
            assert i < n2;
            assert str.length() == i;

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
            assert i >= n2;
            assert i < n1;
            assert str.length() == i;

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

        BigInt b = new BigInt(str);

        assert b.isValid();
        assert b.compareTo(this) <= 0;

        return b;
    }

    public BigInt multiply(BigInt n) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        assert this.isValid();
        assert n.isValid();

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
            assert i < len1;

            int carry = 0;
            int n1 = this.value[i];
            i_n2 = 0;

            for (int j = 0; j < len2; j++)
            {
                assert j < len2;

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

        BigInt b = new BigInt(s);
        assert b.isValid();
        assert (b.compareTo(this) >= 0) || (b.compareTo(new BigInt(0)) == 0 && (this.compareTo(new BigInt(0)) == 0 || n.compareTo(new BigInt(0)) == 0));
        assert (b.compareTo(n) >= 0) || (b.compareTo(new BigInt(0)) == 0 && (this.compareTo(new BigInt(0)) == 0 || n.compareTo(new BigInt(0)) == 0));

        return b;
    }

    public BigInt divide(int divisor) throws DivisionByZeroException, InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        assert this.isValid();

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

        BigInt b = new BigInt(result.substring(i));

        assert b.isValid();
        assert b.compareTo(this) <= 0;
        return new BigInt(result.substring(i));
    }

    public BigInt divide(BigInt divisor) throws DivisionByZeroException, InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, NegativeValueException {
        assert this.isValid();
        assert divisor.isValid();

        BigInt b = new BigInt(this);

        if(divisor.compareTo(new BigInt(0)) == 0) {
            throw new DivisionByZeroException();
        }

        BigInt count = new BigInt(0);

        while(b.compareTo(divisor) >= 0) {
            b = new BigInt(b.subtract(divisor));
            count = count.add(new BigInt(1));
        }

        assert count.isValid();
        assert count.compareTo(this) <= 0;
        return count;
    }

    public BigInt pow(int power) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        assert this.isValid();

        if(power == 0){
            return new BigInt(1);
        }

        BigInt b = new BigInt(this);

        for(int i = 1; i < power; i++){
            b = new BigInt(b.multiply(this));
        }

        assert b.isValid();
        assert b.compareTo(this) >= 0;
        return b;
    }

    public BigInt pow(BigInt power) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, NegativeValueException {
        assert this.isValid();
        assert power.isValid();

        if(power.compareTo(new BigInt(0)) == 0 ) {
            return new BigInt(1);
        }

        BigInt b = new BigInt(this);

        while(!(power.compareTo(new BigInt(1)) == 0)) {
            b = new BigInt(b.multiply(this));
            power = power.subtract(new BigInt(1));
        }

        assert b.isValid();
        assert b.compareTo(this) >= 0;
        return b;
    }

    public BigInt sqrt(int root) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        assert this.isValid();

        for(int i = 1; i < Integer.MAX_VALUE; i++){
            BigInt b = new BigInt(i);
            if(b.pow(root).compareTo(this) > 0){
                return new BigInt(i-1);
            }
        }

        return null;
    }

    public BigInt sqrt(BigInt root) throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, NegativeValueException {
        assert this.isValid();

        for(int i = 1; i < Integer.MAX_VALUE; i++){
            BigInt b = new BigInt(i);
            if(b.pow(root).compareTo(this) > 0){
                BigInt result = new BigInt(i-1);

                assert result.isValid();
                return result;
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
        assert this.isValid();
        String s = "";

        for (int j : value) {
            s = String.valueOf(j).concat(s);
        }

        assert s.length() == this.numberOfDigits;
        assert s.length() == this.value.length;
        return s;
    }

    @Override
    public int compareTo(BigInt b){
        assert this.isValid();
        assert b.isValid();

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
            assert this.value[i] == b.value[i];
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        assert this.isValid();

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BigInt BigInt = (BigInt) o;
        return numberOfDigits == BigInt.numberOfDigits && Arrays.equals(value, BigInt.value);
    }

    @Override
    public int hashCode() {
        assert this.isValid();

        int result = Objects.hash(numberOfDigits);
        result = 31 * result + Arrays.hashCode(value);
        return result;
    }

    @Override
    public String toString() {
        assert this.isValid();

        return this.convertToString();
    }

    private boolean isValid(){
        if(this.numberOfDigits > MAX_NUMBER_OF_DIGITS){
            return false;
        }
        if(this.value.length > MAX_NUMBER_OF_DIGITS){
            return false;
        }

        for (int j : this.value) {
            if (j < 0 || j > 9) {
                return false;
            }
        }

        return this.value.length == this.numberOfDigits;
    }
}
