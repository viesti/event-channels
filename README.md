# Event handling with core.async

# JavaFX #

Initially had an idea to pipe mouse press and move events to separate
channels and combine them with alts!. Since JavaFX allows to observe
events in a single callback (as opposed to many in Swing) the idea
slightly watered down to a local event loop. Neat trial anyway.

# JavaScript #

JavaScript seems to have separate callbacks (like Swing) and unlike
JavaFX, there is more gain in combining those event sources to a
channel and then handling events by type in a go block that drains
events.

# Running #

Both implementations currently let you draw a single never-ending
polyline. Not very usefull. The only excuse I can say is that I had
fun in looking into core.async :)

### Clojure/JavaFX ###

    lein start

### ClojureScript ###

Open `public/index.html` in a browser.

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
