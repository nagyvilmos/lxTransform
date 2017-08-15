/*==============================================================================
 *  Lexa - Property of William Norman-Walker
 * ------------------------------------------------------------------------------
 *  package-info.java (lxTransform)
 * ------------------------------------------------------------------------------
 *  Author:  William Norman-Walker
 *  Created: August 2017
 * ==============================================================================
 */
/**
 * Lexa Transform is used to manipulate the contents of a
 * {@link lexa.core.data.DataSet}. A transform will only manipulate the data
 * required at the output stage.  The transform steps are chained together and
 * from a single parent, multiple branches can be created.
 *
 * <dl>
 * <dt>{@link lexa.core.transform.Transform}</dt>
 * <dd>The root parent of a transform process. This does not perform any
 * transformation but provides the data for all of the subsequent steps.</dd>
 * <dt>{@link lexa.core.transform.Contains}</dt>
 * <dd>Extract items that match the required key.</dd>
 * <dt>{@link lexa.core.transform.Flatten}</dt>
 * <dd>Flatten out the data set on an array field.</dd>
 * <dt>{@link lexa.core.transform.Group}</dt>
 * <dd>Group together data based on the given keys and types.</dd>
 * <dt>{@link lexa.core.transform.Map}</dt>
 * <dd>Apply an {@link lexa.core.expression.map.ExpressionMap} to the data.</dd>
 * <dt>{@link lexa.core.transform.Sort}</dt>
 * <dd>Sort the data based on the given keys.</dd>
 * <dt>{@link lexa.core.transform.Truncate}</dt>
 * <dd>Truncate either the top or tail of the data.</dd>
 * </dl>
 */
package lexa.core.transform;
