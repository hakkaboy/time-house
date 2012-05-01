package com.hakka.pluto.example.module;

public enum Puzzle {
	
	
	PICS_01(0,0),
	PICS_02(0,1),
	PICS_03(0,2),
	PICS_04(0,3),
	PICS_05(1,0),
	PICS_06(1,1),
	PICS_07(1,2),
	PICS_08(1,3),
	PICS_09(2,0),
	PICS_10(2,1),
	PICS_11(2,2),
	PICS_12(2,3),
	PICS_13(3,0),
	PICS_14(3,1),
	PICS_15(3,2),
	PICS_16(3,3),
	PICS_17(4,0),
	PICS_18(4,1),
	PICS_19(4,2),
	PICS_20(4,3);
	
	private int picsX;
	private int picsY;

	Puzzle(int x,int y){
		this.picsX = x;
		this.picsY = y;
	}
	
	public final static int PUZZLE_HEIGHT = 64; //  320px/5
	public final static int PUZZLE_WIDTH = 70;  //  280px/4
	
	public int getTexturePositionX() {
		return this.picsX * PUZZLE_WIDTH;
	}
	
	public int getTexturePositionY() {
		return this.picsY * PUZZLE_HEIGHT;
	}
}
