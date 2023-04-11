package se2.groupb.server.repository;

import java.util.UUID;

public interface EntityRepository<Entity, EntityDTO> {

    /**
     * @param entityID
     * @return entity from database
     */
    public Entity findByID(UUID entityID);

    /**
     * 
     * @param entityDTO
     * @return entity from database
     */
    public Entity findByDTO(EntityDTO entityDTO);

    /**
     * @param entityDto
     * @return
     */
    public Entity findByName(String entityName);

    /**
     * saves entity into database
     * 
     * @return
     */
    boolean save(Entity entity);

    /**
     * updates entity into database
     * 
     * @return
     */
    boolean update(Entity entity);

}
