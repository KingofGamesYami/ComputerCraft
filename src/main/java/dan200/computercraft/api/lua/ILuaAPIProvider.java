package dan200.computercraft.api.lua;

import dan200.computercraft.api.peripheral.IComputerAccess;

public interface ILuaAPIProvider {
    ILuaAPI getLuaAPI( IComputerAccess computer );
}
