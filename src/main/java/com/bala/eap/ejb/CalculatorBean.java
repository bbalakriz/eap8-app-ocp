
package com.bala.eap.ejb;

import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;

@Stateless
@Remote(RemoteCalculator.class)
public class CalculatorBean implements RemoteCalculator {

    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int subtract(int a, int b) {
        return a - b;
    }
}