package com.springboot.rest.aop.aopexample.data;

import org.springframework.stereotype.Repository;

import com.springboot.rest.aop.aopexample.annotations.TrackTime;

@Repository
public class DataService2 {
	
	@TrackTime
	public int[] retrieveData() {
		return new int[] {111, 222, 333, 444, 555};
	}
}
