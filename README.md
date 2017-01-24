# Sanctuary
After you clone the repository you will have to add a new file:

        local.properties

inside this file you need to add a line that points to your local android-sdk:

        sdk.dir=/Users/Scott/android-sdk/

or

        sdk.dir=C:/Users/Scott/Documents/android-sdk/

After you have this you can import into Eclipse following Luke's instructions.

Running from here will result in the error:

        Exception in thread "LWJGL Application" com.badlogic.gdx.utils.GdxRuntimeException: Couldn't load file: icon.png

This is the error Luke described and can be fixed by:

- (In the menu bar) select "Run" -> "Run Configurations"
- Select "Desktop Launcher" on the left (under "Java Applications")
- Select the "Arguments" tab in the middle
- At the bottom select to "Other" radio button and click workspace
- In the window that pops up, expand "sanctuary-android" and select "assets"
- "OK" out of everything and you should be good to go.
