package org.b3log.symphony.repository;

import org.b3log.latke.repository.AbstractRepository;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.annotation.Repository;
import org.b3log.symphony.model.Tag;
import org.json.JSONObject;

import java.util.List;

@Repository
public class TagTimeRepository extends AbstractRepository
{
    /**
     * Constructs a repository with the specified name.
     *
     */
    public TagTimeRepository() {
        super(Tag.TAG + "_" + Tag.TIME);
    }

    @Override
    public List<JSONObject> getList(Query query) throws RepositoryException {
        return null;
    }
}
