package main;

public class Inventory {

    Weapon[] weapons;

    public Inventory(int size) {
        weapons = new Weapon[size];
    }

    public boolean addWeapon(Weapon w) {
        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) {
                weapons[i] = w;
                return true;
            }
        }
        return false;
    }

    public Weapon getWeapon(int index) {
        if (index < 0 || index >= weapons.length)
            return null;
        return weapons[index];
    }

    public int size() {
        return weapons.length;
    }
}
