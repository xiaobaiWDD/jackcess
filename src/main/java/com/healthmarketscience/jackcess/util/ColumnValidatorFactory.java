/*
Copyright (c) 2014 James Ahlborn

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

*/

package com.healthmarketscience.jackcess.util;

import com.healthmarketscience.jackcess.Column;

/**
 * Factory which generates appropriate ColumnValidators when Column instances
 * are created.
 *
 * @author James Ahlborn
 */
public interface ColumnValidatorFactory 
{
  /**
   * Returns a ColumnValidator instance for the given column, or {@code null}
   * if the default should be used.
   */
  public ColumnValidator createValidator(Column col);
}