package se2.groupb.server.repository;

import java.util.UUID;

// TODO: see if we can add the dummy data for the customer here

public interface EntityRepository<Entity, EntityDTO> {

    /**
     * @param customerID
     * @return customer from database
     */
    public Entity findByID(UUID entityID);

    /**
     * 
     * @param customerDto
     * @return customer from database
     */
    public Entity findByDTO(EntityDTO entityDTO);

    /**
     * @param customerDto
     * @return
     */
    public Entity findByName(String entityName);

    /**
     * saves customer into database
     * 
     * @return
     */
    boolean save(Entity entity);

    /**
     * updates customer into database
     * 
     * @return
     */
    boolean update(Entity entity);

}
