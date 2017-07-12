/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2017. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.computercraft.api.lua;

import dan200.computercraft.api.peripheral.IComputerAccess;

public interface ILuaAPIProvider {
    ILuaAPI getLuaAPI( IComputerAccess computer );
}
