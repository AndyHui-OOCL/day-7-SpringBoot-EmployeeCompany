//package org.example.demo.controller;
//
//import org.example.demo.entity.ParkingBoy;
//import org.example.demo.service.ParkingBoyService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/v1/parking-boys")
//public class ParkingBoyController() {
//    @Autowired
//    private ParkingBoyService parkingBoyService;
//
//    @PostMapping
//    public ParkingBoy createParkingBoy(@RequestBody ParkingBoy parkingBoy) {
//        return parkingBoyService.createParkingBoy(parkingBoy);
//    }
//
//    @GetMapping
//    public List<ParkingBoy> getAllParkingBoy() {
//        return parkingBoyService.getAllParkingBoy();
//    }
//
//}
