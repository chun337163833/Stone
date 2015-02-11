package com.stone.db.agent;

import java.util.List;

import com.stone.core.util.LRUHashMap;
import com.stone.core.util.ModifiedSet;
import com.stone.db.cache.HumanCache;
import com.stone.db.entity.HumanEntity;

/**
 * Human agent;
 * 
 * @author crazyjohn
 *
 */
public class HumanAgent extends BaseEntityAgent implements ICacheEntityAgent<HumanEntity, Long, HumanCache> {
	private ModifiedSet<HumanEntity> modifySet = new ModifiedSet<>();
	private LRUHashMap<Long, HumanCache> humanCaches;

	public HumanAgent(int maxCacheSize) {
		// FIXME: crazyjohn evictPolicy do not use default?
		humanCaches = new LRUHashMap<Long, HumanCache>(maxCacheSize, null);
	}

	@Override
	public List<HumanEntity> getModifiedEntities(int size) {
		return modifySet.getModified(size);
	}

	@Override
	public HumanCache getFromCache(Long id) {
		return humanCaches.get(id);
	}

	@Override
	public void setToCache(Long id, HumanCache cacheObject) {
		this.humanCaches.put(id, cacheObject);
	}

}