package com.example.ppo2;

import android.graphics.Color;

public class Sequence
{
    public int id;
    public int work;
    public int color;
    public int cycles;
    public String title;
    public int prepare;
    public int stsCalm;
    public int calm;
    public int sets;


    Sequence(int id, int color, String title, int prepare, int work, int calm, int cycles, int sets, int stsCalm)
    {
        this.id = id;
        this.work = work;
        this.color = color;
        this.cycles = cycles;
        this.title = title;
        this.prepare = prepare;
        this.stsCalm = stsCalm;
        this.calm = calm;
        this.sets = sets;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
