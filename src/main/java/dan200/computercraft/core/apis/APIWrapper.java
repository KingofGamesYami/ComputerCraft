package dan200.computercraft.core.apis;

import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.filesystem.IWritableMount;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.core.computer.Computer;
import dan200.computercraft.core.filesystem.FileSystem;
import dan200.computercraft.core.filesystem.FileSystemException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by steve on 7/11/2017.
 */
public class APIWrapper implements IComputerAccess {
    private Set<String> m_mounts;

    private final IAPIEnvironment m_environment;
    private FileSystem m_fileSystem;
    private boolean m_running;

    public APIWrapper( Computer computer )
        {
            m_environment = computer.getAPIEnvironment();
            m_mounts = new HashSet<String>();
        }

        // IComputerAccess implementation


    @Nullable
    @Override
    public String mount(@Nonnull String desiredLocation, @Nonnull IMount mount) {
        return mount( desiredLocation, mount, "api" );
    }

    @Override
        public synchronized String mount( @Nonnull String desiredLoc, @Nonnull IMount mount, @Nonnull String driveName )
        {

            // Mount the location
            String location;
            synchronized( m_fileSystem )
            {
                location = findFreeLocation( desiredLoc );
                if( location != null )
                {
                    try {
                        m_fileSystem.mount( driveName, location, mount );
                    } catch( FileSystemException e ) {
                        // fail and return null
                    }
                }
            }
            if( location != null )
            {
                m_mounts.add( location );
            }
            return location;
        }

        @Override
        public String mountWritable( @Nonnull String desiredLoc, @Nonnull IWritableMount mount )
        {
            return mountWritable( desiredLoc, mount, "api" );
        }

        @Override
        public synchronized String mountWritable( @Nonnull String desiredLoc, @Nonnull IWritableMount mount, @Nonnull String driveName )
        {
            // Mount the location
            String location;
            synchronized( m_fileSystem )
            {
                location = findFreeLocation( desiredLoc );
                if( location != null )
                {
                    try {
                        m_fileSystem.mountWritable( driveName, location, mount );
                    } catch( FileSystemException e ) {
                        // fail and return null
                    }
                }
            }
            if( location != null )
            {
                m_mounts.add( location );
            }
            return location;
        }

        @Override
        public synchronized void unmount( String location )
        {
            if( location != null )
            {
                if( !m_mounts.contains( location ) ) {
                    throw new RuntimeException( "You didn't mount this location" );
                }

                m_fileSystem.unmount( location );
                m_mounts.remove( location );
            }
        }

        @Override
        public synchronized int getID()
        {
            return m_environment.getComputerID();
        }

        @Override
        public synchronized void queueEvent( @Nonnull final String event, final Object[] arguments )
        {
            m_environment.queueEvent( event, arguments );
        }

        @Nonnull
        @Override
        public synchronized String getAttachmentName()
        {
            return "api";
        }
    private String findFreeLocation( String desiredLoc )
    {
        try
        {
            synchronized( m_fileSystem )
            {
                if( !m_fileSystem.exists( desiredLoc ) )
                {
                    return desiredLoc;
                }
                // We used to check foo2,foo3,foo4,etc here
                // but the disk drive does this itself now
                return null;
            }
        }
        catch( FileSystemException e )
        {
            return null;
        }
    }
}
