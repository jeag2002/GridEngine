package es.grid.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.grid.client.bean.BWrapper;
import es.grid.client.service.SudokuService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Controller Method for resolving sudokus.
 * 
 * 1- Call Grid engine for solving sudokus ==> hazelcast(?)
 * 2- Cache engine hazelcast? 
 * @author Usuario
 */

@RestController
public class SudokuController {
	
	@Autowired
	SudokuService sService;
	
	private final Logger logger = LoggerFactory.getLogger(SudokuController.class);
	
	@RequestMapping(value = "/resolveSudoku", method = RequestMethod.POST)
    public ResponseEntity<BWrapper> resolveSudoku(@RequestBody BWrapper requestWrapper) {
		logger.info("[SudokuControler] -- resolveSudoku POST");
		BWrapper responseWrapper = sService.solveSudoku(requestWrapper);
		return new ResponseEntity<BWrapper>(responseWrapper, HttpStatus.OK);
    }
	
}
