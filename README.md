# CompositeDesginPattern

Overview:
Composite pattern is a structural design pattern which is used to apply common methods to a collection of objects where a partial or complete hierarchy can be established. Simply speaking, if a client wishes to interact with components which have a tree relationship with each other while being agnostic of whether the component is a parent or a child, we make use of composite design pattern to achieve extensibility and modularity in our code

Introduction:
Let us say we have been asked to design a stargazing app. One of the basic functionalities expected out of such an app is to print all the stars there are in a galaxy. We can establish the following hierarchy of objects in our

system: Universe -> Galaxy -> Star

We can now come up with the following 3 Java classes

public class Star {
    String name;
}

public class Galaxy {
    String name;
    List<Star> stars;
}

public class Universe {
    String name;
    List<Galaxy> galaxies
}

Each class holds a list of objects of its child class.

Now, we have a requirement to introduce a Constellation in our app. A Constellation resides within a Galaxy, and is a collection of stars. Essentially we can treat it like a sub galaxy for better understanding. How do we incorporate this in our design?

public class Constellation {
    String name;
    List<Star> stars;
}

Let us say we make the following change to our Galaxy class

public class Galaxy {
    String name;
    List<Constellation> constellations;
}

However there can be stars that are not part of any constellation, ie stars is not a strict subset of a constellation. Now we make the following change to our Galaxy class:

public class Galaxy {
    String name;
    List<Star> stars;
    List<Constellation> constellations;
    
    void print(){
        System.out.println("GALAXY = " + galaxy.getName());
        for(Star star: stars){
            System.out.println("STAR = " + star.getName());
        }
        for(Constellation constellation: constellations){
            System.out.println("CONSTELLATION = " + constellation.getName());
            for(Star star: constellation.getStars()){
                System.out.println("STAR = " + star.getName());
            }
        }
    }
}

If tomorrow we have a new requirement of a collection of twin stars let's say, we will again end up editing the Galaxy class (or some other), and thus end up breaking one of the basic principles of good OO design: The Open-closed principle, which states that classes should be closed for modification but open for extension. We might unknowingly end up introducing bugs in our system by touching the legacy classes, which might be involved in a lot of application code.

We need to come up with a smart way to introduce new classes in our hierarchy and here is where Composite pattern comes into play.

How does the Composite Pattern work?
Let us try to take out the printing logic from our core classes and delegate it to some other class, as the printing logic is something that is susceptible to change.

We have the following requirements from our application:

1. Operations like print() can be applied on the whole hierarchy (the whole Universe) or on a part (a constellation or a star).
2. Our client class should not worry if it is operating on a parent node or a child node in our hierarchy tree.

Let us define the basic functionalities we expect our tree as a whole to support in the following abstract class:

public abstract class UniverseComponent {
    
    getName(){
        throw new UnsupportedOperationException();
    }
    
    getType(){
         throw new UnsupportedOperationException();
    }
    
    getChild(int i){
         throw new UnsupportedOperationException();
    }
    
    addChild(UniverseComponent universeComponent){
         throw new UnsupportedOperationException();
    }
    
    removeChild(UniverseComponent universeComponent){
         throw new UnsupportedOperationException();
    }
}

You will soon see why we are throwing exceptions as a default implementation.

Now we can just define two classes, one a leaf and one parent node which will extend the above abstract class.

public class Star extends UniverseComponent {
    
    String name;
    String type;
    
    Star(String name, String type){
        this.name = name;
        this.type = type;
    }
    
    getName(){
        return this.name;
    }
    
    getType(){
        return this.type;
    }
    
    print(){
        System.out.println(this.type + " = " + this.name);
    }
}

public class StarCollection extends UniverseComponent {

    String name;
    String type;
    List<UniverseComponent> universeComponents; // A component can hold other components within it

    StarCollection(String name, String type) {
        this.name = name;
        this.type = type;
        this.universeComponents = new ArrayList<>();
    }

    String getName() {
        return this.name;
    }

    String getType() {
        return this.type;
    }

    UniverseComponent getChild(int i) {
        return universeComponents.get(i);
    }

    void addChild(UniverseComponent component) {
        universeComponents.add(component);
    }

    void removeChild(UniverseComponent component) {
        universeComponents.remove(component);
    }

    void print() {
        System.out.println(this.type + " = " + this.name);
        for (UniverseComponent component : this.universeComponents) {
            component.print();
        }
    }
}

We can now summarize the above design in a class diagram.


Let us now see the composite pattern in action by writing a main class.

public static void main(String[] args) {
    
        UniverseComponent universe = new StarCollection("The whole universe", "UNIVERSE");
        UniverseComponent milkway = new StarCollection("Milky way", "GALAXY");
        UniverseComponent andromeda = new StarCollection("Andromeda", "GALAXY");
        UniverseComponent canisMajor = new StarCollection("Canis Major", "CONSTELLATION");
        UniverseComponent sirius = new Star("Sirius", "STAR");
        UniverseComponent sun = new Star("Sun", "STAR");
        UniverseComponent mirach = new Star("Mirach", "STAR");
        UniverseComponent alpheratz = new Star("Alpheratz", "STAR");

        universe.addChild(milkway);
        universe.addChild(andromeda);
        milkway.addChild(canisMajor);
        canisMajor.addChild(sirius);
        milkway.addChild(sun);
        andromeda.addChild(mirach);
        andromeda.addChild(alpheratz);
        universe.print();
}

Output:

UNIVERSE = The whole universe
GALAXY = Milky way
CONSTELLATION = Canis Major
STAR = Sirius
STAR = Sun
GALAXY = Andromeda
STAR = Mirach
STAR = Alpheratz

Thus, the client class need not worry about how the hierarchy in the system is implemented, or whether it is interacting with a child component or a parent. Any sort of new composition can be easily implemented without modifying the classes as long as the class extends UniverseComponent.

Pros and Cons of Composite Design Pattern:
Pros:

Composite pattern helps clients become agnostic of the difference between compositions of objects and individual objects.
Objects can be shared and re-used as all objects extend a common class, thus reducing memory footprint of the application, reducing object creation time and garbage collection time.

Cons:

Composite pattern introduces tight hierarchical pattern in the design and enforces new classes to adhere to similar template of already created classes.
It is hard to introduce a custom operation specific to a composition or an individual type.

Difference between Composite Pattern and Decorator Pattern:

Composite 
Decorator
Composite pattern allows you to build a hierarchical structure
Decorator patterns allows you to completely contain one entity into another
The interface of a leaf entity is exactly the same as the composite entity
The interface of an entity containing another might be different
The client views the composition as a whole single entity
Using decorator as well, the outward appearance of a class does not change


FAQ
1. When should I use composite pattern?
When there is an overlap between the implementations of your classes and they can be represented as a part-whole hierarchy, it is a good idea to use Composite Design Pattern.

2. My design is not allowing me to implement the Composite Pattern, what should I do?
It is a wise idea to re-work the design at the initial stages so that later addition of features becomes easier. Saving time now by preventing design changes will cause a lot of maintenance and operational costs in the future. Composite pattern is one such pattern that needs to be envisioned at the start of an application's life cycle.
