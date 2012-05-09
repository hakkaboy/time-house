package com.hakka.pluto.example.module;

import org.andengine.entity.shape.IShape;

public enum Puzzle2 implements IPuzzle {
	
	
	PICS_00(0,0),
	PICS_01(1,0),
	PICS_02(2,0),
	PICS_03(3,0),
	PICS_04(4,0),
	PICS_14(0,1),
	PICS_10(1,1),
	PICS_11(2,1),
	PICS_12(3,1),
	PICS_13(4,1),
	PICS_20(0,2),
	PICS_21(1,2),
	PICS_22(2,2),
	PICS_23(3,2),
	PICS_24(4,2);

	private int picsX;
	private int picsY;
	private IShape answerShape;

	Puzzle2(int x,int y){
		this.picsX = x;
		this.picsY = y;
	}
	
	public final static int PUZZLE_HEIGHT = 111; //  320px/5
	public final static int PUZZLE_WIDTH = 100;  //  280px/4
	
	public int getTexturePositionX() {
		return this.picsX * PUZZLE_WIDTH;
	}
	
	public int getTexturePositionY() {
		return this.picsY * PUZZLE_HEIGHT;
	}
	
	public void setAnswer(IShape ansArea){
		this.answerShape = ansArea;
	}
	
	public IShape getAnswer(){
		return this.answerShape;
	}
	
	public static String getResourceName(){
		return "amber.jpeg";
	}
}
