package com.knowshare.api.controller.leaderboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.knowshare.dto.ludificacion.CarreraLeaderDTO;
import com.knowshare.enterprise.bean.leaderboard.LeaderFacade;

/**
 * 
 * @author Felipe Bautista
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/leader")
public class LeaderBoardController {

	@Autowired
	private LeaderFacade leaderBean;
	
	@RequestMapping(value="/findAll", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<CarreraLeaderDTO>> getAllCarreras(){
		List<CarreraLeaderDTO> carreras = leaderBean.CarrerasLeader();
		if(carreras == null || carreras.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
			.body(null);
		return ResponseEntity.status(HttpStatus.OK)
				.body(carreras);
	}
	
}
