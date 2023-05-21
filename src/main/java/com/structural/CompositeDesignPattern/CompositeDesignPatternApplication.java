package com.structural.CompositeDesignPattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompositeDesignPatternApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompositeDesignPatternApplication.class, args);
		Directory movieDirectory = new Directory("Movies");
		FileSystem spiderman = new File("SpiderMan");
		movieDirectory.add(spiderman);
		
		Directory actionMovieDirectory = new Directory("ActionMovies");
		FileSystem fastAndFurious = new File("FastAndFurious");
		actionMovieDirectory.add(fastAndFurious);
		FileSystem topGun = new File("topGun");
		actionMovieDirectory.add(topGun);
		movieDirectory.add(actionMovieDirectory);
		
		movieDirectory.ls();
	}
}
