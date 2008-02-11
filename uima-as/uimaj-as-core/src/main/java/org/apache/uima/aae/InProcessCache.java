/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.aae;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.controller.ControllerLifecycle;
import org.apache.uima.aae.controller.Endpoint;
import org.apache.uima.aae.controller.EventSubscriber;
import org.apache.uima.aae.error.AsynchAEException;
import org.apache.uima.aae.message.MessageContext;
import org.apache.uima.aae.monitor.statistics.DelegateStats;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.OutOfTypeSystemData;
import org.apache.uima.cas.impl.XmiSerializationSharedData;
import org.apache.uima.flow.Step;
import org.apache.uima.util.Level;

public class InProcessCache implements InProcessCacheMBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Class CLASS_NAME = InProcessCache.class;

	private transient UIDGenerator idGenerator = new UIDGenerator();
//	private Map cache = new HashMap();
	private ConcurrentHashMap cache = new ConcurrentHashMap();

	private String name = "InProcessCache";
	private List callbackListeners = new ArrayList();
	
	int size = 0;
	
	public void registerCallbackWhenCacheEmpty(EventSubscriber aController )
	{
		if ( !callbackListeners.isEmpty() )
		{
			Iterator it = callbackListeners.iterator();
			while( it.hasNext() )
			{
				EventSubscriber es = (EventSubscriber)it.next();
				if ( es == aController )
				{
					return;
				}
			}
		}
		callbackListeners.add(aController);
	}
	public void destroy()
	{
		callbackListeners.clear();
		Set set = cache.entrySet();
		for( Iterator it = set.iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry)it.next();
			CacheEntry cacheEntry = (CacheEntry)entry.getValue();
			if ( cacheEntry != null && cacheEntry.getCas() != null )
			{
				try
				{
					cacheEntry.getCas().release();
				}
				catch( Exception e){}
			}
		}
		
		cache.clear();
	}
	
	/**
	 * Checks if a given input CAS is in pending state. CAS is in pending state if it has been
	 * fully processed, *but* its subordinate CASes are still in play. Input CAS is only
	 * returned back to the client if all if its subordinate CASes are fully processed. 
	 * 
	 * @param anInputCASReferenceId
	 * @return
	 * @throws Exception
	 */
	public boolean isInputCASPendingReply( String anInputCASReferenceId ) throws Exception
	{
    if ( anInputCASReferenceId == null )
    {
      return false;
    }
    CacheEntry inputCASEntry =
      getCacheEntryForCAS( anInputCASReferenceId );

    return inputCASEntry.isPendingReply();
	}
	
	public boolean producedCASesStillInPlay( String anInputCASReferenceId, String aSubordinateCASReferenceId ) throws Exception
	{
    Iterator it = cache.keySet().iterator();
    while( it.hasNext())
    {
      String key = (String) it.next();
      CacheEntry entry = (CacheEntry) cache.get(key);
      if ( entry != null && aSubordinateCASReferenceId != null && aSubordinateCASReferenceId.equals(key))
      {
        continue;   // dont count the current subordinate
      }
      if ( entry != null && anInputCASReferenceId.equals( entry.getInputCasReferenceId()))
      {
        return true;
      }
    }
	  return false;
	}
	public void releaseCASesProducedFromInputCAS( String anInputCASReferenceId )
	{
		if ( anInputCASReferenceId == null )
		{
			return;
		}
		Iterator it = cache.keySet().iterator();
		while( it.hasNext())
		{
			String key = (String) it.next();
			CacheEntry entry = (CacheEntry) cache.get(key);
			if ( entry != null && (anInputCASReferenceId.equals( key ) || anInputCASReferenceId.equals( entry.getInputCasReferenceId())))
			{
				if ( entry.getCas() != null )
				{
					entry.getCas().release();
				}
				remove(key);
			}
		}
		
	}
	public void releaseAllCASes()
	{
		Iterator it = cache.keySet().iterator();
		while( it.hasNext())
		{
			String key = (String) it.next();
			CacheEntry entry = (CacheEntry) cache.get(key);
			if ( entry != null && entry.getCas() != null )
			{
				entry.getCas().release();
			}
			cache.remove(key);
		}

	}
	public void setName( String aName )
	{
		name = aName;
	}
	public String getName()
	{
		return name;
	}
	public void cancelTimers()
	{
		Iterator it = cache.keySet().iterator();
		String key;
		while( it.hasNext() )
		{
			key = (String)it.next();
			CacheEntry entry = (CacheEntry)cache.get(key);
			if ( entry != null )
			{
				Map em = entry.getEndpointMap();
				if ( em != null )
				{
					Iterator it2 = em.keySet().iterator();
					String endpointKey ="";

					while( it2.hasNext())
					{
						endpointKey = (String)it2.next();
						Endpoint end = (Endpoint)em.get(endpointKey);
					}
				}
			}
		}
	}
	public boolean isEmpty()
	{
		return (cache.size() == 0);
	}
	public void reset()
	{
		size = 0;
	}
	public int getSize()
	{
		return cache.size();
	}
	
	public void setSize( int i)
	{
		size = i;
	}
	public synchronized void dumpContents()
	{
		if ( UIMAFramework.getLogger().isLoggable(Level.FINEST) )
		{
			int count=0;
			Iterator it = cache.keySet().iterator();

			StringBuffer sb = new StringBuffer("\n");
			while( it.hasNext() )
			{
				String key = (String) it.next();
				CacheEntry entry = (CacheEntry)cache.get(key);
				count++;
				sb.append(key+"\n");
			}
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
	                "dumpContents", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_show_cache_entry_key__FINEST",
	                new Object[] { count, sb.toString() });
			sb.setLength(0);
/*			
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINEST, CLASS_NAME.getName(),
	                "dumpContents", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_show_cache_size__FINEST",
	                new Object[] { count });
*/
		}
	}
	public synchronized void remove(String aCasReferenceId)
	{
		if (aCasReferenceId != null && cache.containsKey(aCasReferenceId))
		{
			
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "remove", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_remove_cache_entry_for_cas__FINE", new Object[] { aCasReferenceId });
			cache.remove(aCasReferenceId);
			this.notifyAll();
		}
		else if ( aCasReferenceId == null )
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "remove", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cas_is_null_remove_from_cache_failed__FINE");
		}
		else
		{
			UIMAFramework.getLogger(CLASS_NAME).logrb(Level.FINE, getClass().getName(), "remove", UIMAEE_Constants.JMS_LOG_RESOURCE_BUNDLE, "UIMAEE_cas_is_invalid_remove_from_cache_failed__FINE", new Object[] { aCasReferenceId });
		}
		if ( isEmpty() && callbackListeners.size() > 0 )
		{
			for( int i=0; i < callbackListeners.size(); i++ )
			{
				((EventSubscriber)callbackListeners.get(i)).onCacheEmpty();
			}
		}

	}
	public void removeCas(String aCasReferenceId)
	{
		if (!cache.containsKey(aCasReferenceId))
		{
			return;
		}
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		casRefEntry.deleteCAS();
	}

	public CacheEntry[] getCacheEntriesForEndpoint(String anEndpointName )
	{
		CacheEntry[] entries;
		ArrayList list = new ArrayList();
		Iterator it = cache.keySet().iterator();
		while( it.hasNext() )
		{
			String key = (String)it.next();
			CacheEntry entry = (CacheEntry)cache.get(key);
			if ( entry != null && entry.getEndpoint(anEndpointName) != null )
			{
				list.add(entry);
			}
		}
		if (list.size() > 0 )
		{
			entries = new CacheEntry[list.size()];
			list.toArray(entries);
			return entries;
		}
		return null;
	}
	public void saveSerializedCAS(String aCasReferenceId, String anXCAS)
	{
		if (!cache.containsKey(aCasReferenceId))
		{
			return;
		}
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		casRefEntry.setSerializedCas(anXCAS);
	}

	public String getSerializedCAS(String aCasReferenceId)
	{
		if (cache.containsKey(aCasReferenceId))
		{
			CacheEntry casRefEntry = getEntry(aCasReferenceId);
			return casRefEntry.getSerializedCas();
		}
		return null;
	}
	
	public void setCasProducer( String aCasReferenceId, String aCasProducerKey)
	{
		if (cache.containsKey(aCasReferenceId))
		{
			CacheEntry casRefEntry = getEntry(aCasReferenceId);
			casRefEntry.setCasProducerKey(aCasProducerKey);
		}
		
	}

	public String getCasProducer( String aCasReferenceId)
	{
		if (cache.containsKey(aCasReferenceId))
		{
			CacheEntry casRefEntry = getEntry(aCasReferenceId);
			return casRefEntry.getCasProducerKey();
		}
		return null;
	}

	public synchronized CAS getCasByReference(String aCasReferenceId)
	{
		if (!cache.containsKey(aCasReferenceId))
		{
			return null;
		}
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		return casRefEntry.getCas();
	}

	public MessageContext getMessageAccessorByReference(String aCasReferenceId)
	{
		if (!cache.containsKey(aCasReferenceId))
		{
			return null;
		}
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		return casRefEntry.getMessageAccessor();
	}

	public OutOfTypeSystemData getOutOfTypeSystemData(String aCasReferenceId)
	{
		if (!cache.containsKey(aCasReferenceId))
		{
			return null;
		}
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		return casRefEntry.getOtsd();
	}
	private CacheEntry getEntry(String aCasReferenceId)
	{
		return (CacheEntry) cache.get(aCasReferenceId);
	}
	public void addEndpoint( Endpoint anEndpoint, String aCasReferenceId)
	{
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		casRefEntry.addEndpoint(anEndpoint);
		
		
	}

	public Endpoint getEndpoint(String anEndpointName, String aCasReferenceId)
	{
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		if ( anEndpointName == null && casRefEntry != null)
		{
			return casRefEntry.getMessageOrigin();
		}
        if ( casRefEntry == null )
        {
        	return null;
        }
        else
        {
    		return casRefEntry.getEndpoint(anEndpointName);
        }
	}
	public void removeEndpoint( String anEndpointName, String aCasReferenceId)
	{
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		casRefEntry.deleteEndpoint( anEndpointName);
	}

	public long getStartTime(String aCasReferenceId)
	{
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		return casRefEntry.getStartTime();
	}
	public synchronized String register(String anInputCasRefId, long aCurrentSequence, CAS aCAS, MessageContext aMessageContext, OutOfTypeSystemData otsd)
	throws AsynchAEException
	{
		String casReferenceId = anInputCasRefId+"."+String.valueOf(aCurrentSequence); 

		register(aCAS, aMessageContext, otsd, casReferenceId);
		return casReferenceId;
	}
	public synchronized String register(CAS aCAS, MessageContext aMessageContext, OutOfTypeSystemData otsd)
	throws AsynchAEException
	{
		//System.out.println("Register");
		String casReferenceId = idGenerator.nextId(); 
		register(aCAS, aMessageContext, otsd, casReferenceId);
		return casReferenceId;
	}
	
	public synchronized String register(CAS aCAS, MessageContext aMessageContext, XmiSerializationSharedData sharedData)
	throws AsynchAEException
	{
		//System.out.println("Register");
		String casReferenceId = idGenerator.nextId(); 
		register(aCAS, aMessageContext, sharedData, casReferenceId);
		return casReferenceId;
	}	
	public boolean entryExists(String aCasReferenceId) 
	{
		try
		{
			CacheEntry casRefEntry = getEntry(aCasReferenceId);
			if ( casRefEntry == null )
			{
				return false;
			}
		}
		catch( Exception e)
		{
			//	ignore
		}
		return true;
	}
	public synchronized void register(CAS aCAS, MessageContext aMessageContext, OutOfTypeSystemData otsd, String aCasReferenceId)
	throws AsynchAEException
	{
		cache.put(aCasReferenceId, new CacheEntry(aCAS, aMessageContext, otsd));
	}
	
	
	public synchronized void register(CAS aCAS, MessageContext aMessageContext, XmiSerializationSharedData sharedData, String aCasReferenceId)
	throws AsynchAEException
	{
		cache.put(aCasReferenceId, new CacheEntry(aCAS, aCasReferenceId, aMessageContext, sharedData));
	}	
	public void register(CAS aCAS, OutOfTypeSystemData otsd, String aCasReferenceId ) 
	throws AsynchAEException
	{
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		if ( casRefEntry == null )
		{
			throw new AsynchAEException("Cas Not Found In CasManager Cache. CasReferenceId::"+aCasReferenceId+" is Invalid");
		}
		casRefEntry.setCas(aCAS, otsd);
	}
	public void register(CAS aCAS, String aCasReferenceId ) 
	throws AsynchAEException
	{
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		if ( casRefEntry == null )
		{
			throw new AsynchAEException("Cas Not Found In CasManager Cache. CasReferenceId::"+aCasReferenceId+" is Invalid");
		}
		casRefEntry.setCas(aCAS);
	}
	
	public int getNumberOfParallelDelegates(String aCasReferenceId)
	throws AsynchAEException
	{
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		if ( casRefEntry == null )
		{
			throw new AsynchAEException("Cas Not Found In CasManager Cache. CasReferenceId::"+aCasReferenceId+" is Invalid");
		}
		return casRefEntry.getNumberOfParallelDelegates();
	}
 
	public void setNumberOfParallelDelegates(int aParallelDelegateCount, String aCasReferenceId)
	throws AsynchAEException
	{
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		if ( casRefEntry == null )
		{
			throw new AsynchAEException("Cas Not Found In CasManager Cache. CasReferenceId::"+aCasReferenceId+" is Invalid");
		}
		casRefEntry.setNumberOfParallelDelegates(aParallelDelegateCount);
	}
	
	public CacheEntry getCacheEntryForCAS( String aCasReferenceId )
	throws AsynchAEException
	{
		CacheEntry casRefEntry = getEntry(aCasReferenceId);
		if ( casRefEntry == null )
		{
			throw new AsynchAEException("Cas Not Found In CasManager Cache. CasReferenceId::"+aCasReferenceId+" is Invalid");
		}
		return casRefEntry;
	}
	
	public class CacheEntry
	{
		private CAS cas;

		//	the following is set to true if the CAS has been created by CAS Multiplier
		//	This flag is used to determine if the CAS should be output to client.
		private boolean newCas;
		
		private String casReferenceId;
		
		//	This is set if the CAS was produced by a Cas Multiplier
		private String inputCasReferenceId;

		//	This is Cas Reference Id of the CAS produced by Remote CM
		private String remoteCMCasReferenceId;
		
		private DelegateStats stats;
		
		private int numberOfParallelDelegates = 1;
		
		private int howManyDelegatesResponded = 0;
		
		private MessageContext messageAccessor;

		private OutOfTypeSystemData otsd = null;

		private String serializedCas;
		
		private String casProducerKey;
		
		private Map endpointMap = new HashMap();
		
		private final long timeIn = System.nanoTime(); 
		
		private Endpoint messageOrigin;
		
		private Stack originStack = new Stack();
		
		private int highWaterMark;
    
    private XmiSerializationSharedData deserSharedData;
		
		private String aggregateProducingTheCas;
		
		private long timeWaitingForCAS = 0;
		
		private long timeToDeserializeCAS = 0;
		
		private long timeToSerializeCAS = 0;
		
		private long timeToProcessCAS = 0;
		private String casMultiplierKey;
		
		private boolean sendRequestToFreeCas = true;
		
		private boolean aborted = false;

		private boolean pendingReply = false;
		
		protected CacheEntry(CAS aCas, MessageContext aMessageAccessor, OutOfTypeSystemData aotsd)
		{
			cas = aCas;
			messageAccessor = aMessageAccessor;
			otsd = aotsd;
			if ( aMessageAccessor != null )
			{
				messageOrigin = aMessageAccessor.getEndpoint();
				
				//originStack.push(aMessageAccessor.getEndpoint());
				
			}
		}
		protected CacheEntry(CAS aCas, String aCasReferenceId, MessageContext aMessageAccessor, XmiSerializationSharedData sdata)
		{
			cas = aCas;
			messageAccessor = aMessageAccessor;
			deserSharedData = sdata;
			if ( aMessageAccessor != null )
			{
				messageOrigin = aMessageAccessor.getEndpoint();
			}
			casReferenceId = aCasReferenceId;
		}
		
		public String getCasReferenceId()
		{
			return casReferenceId;
		}

		public Map getEndpointMap()
		{
			return endpointMap;
		}
		public String getInputCasReferenceId()
		{
			return inputCasReferenceId;
		}
		
		public void setInputCasReferenceId(String anInputCasReferenceId)
		{
			inputCasReferenceId = anInputCasReferenceId;
		}
		
		public void setStat( DelegateStats aStat)
		{
			stats = aStat;
		}
		public DelegateStats getStat()
		{
			return stats;
		}
		public void incrementTimeWaitingForCAS( long aTimeWaitingForCAS)
		{
			timeWaitingForCAS += aTimeWaitingForCAS;
		}
		public void incrementTimeToDeserializeCAS(long aTimeToDeserializeCAS)
		{
			timeToDeserializeCAS += aTimeToDeserializeCAS;
		}
		public void incrementTimeToProcessCAS(long aTimeToProcessCAS)
		{
			timeToProcessCAS += aTimeToProcessCAS;
		}

		public void setCasMultiplierKey( String aKey )
		{
			casMultiplierKey = aKey;
		}
		
		public String getCasMultiplierKey()
		{
			return casMultiplierKey;
		}
		public void incrementTimeToSerializeCAS(long aTimeToSerializeCAS)
		{
			timeToSerializeCAS += aTimeToSerializeCAS;
		}
		public long getTimeWaitingForCAS()
		{
			return timeWaitingForCAS;
		}
		public long getTimeToDeserializeCAS()
		{
			return timeToDeserializeCAS;
		}
		public long getTimeToSerializeCAS()
		{
			return timeToSerializeCAS;
		}

		public synchronized void incrementHowManyDelegatesResponded()
		{
			howManyDelegatesResponded++;
		}
		public synchronized int howManyDelegatesResponded()
		{
			return howManyDelegatesResponded;
		}
		
		public synchronized void resetDelegateResponded()
		{
			howManyDelegatesResponded = 0;
		}
		public void setNumberOfParallelDelegates( int aNumberOfParallelDelegates )
		{
			numberOfParallelDelegates = aNumberOfParallelDelegates;
		}
		
		public int getNumberOfParallelDelegates()
		{
			return numberOfParallelDelegates;
		}
		
		public Endpoint getMessageOrigin()
		{
			//Endpoint ep = (Endpoint)originStack.pop();
			return messageOrigin;
		}
		public void addOrigin( Endpoint anEndpoint)
		{
			originStack.push(anEndpoint);
		}
		protected long getStartTime()
		{
			return timeIn;
		}
		protected void addEndpoint( Endpoint anEndpoint )
		{
			endpointMap.put(anEndpoint.getEndpoint(), anEndpoint);
		}
		
		protected Endpoint getEndpoint(String anEndpointName )
		{
			return (Endpoint)endpointMap.get(anEndpointName);
		}
		protected void deleteEndpoint( String anEndpointName )
		{
			if ( endpointMap.containsKey(anEndpointName))
			{
				endpointMap.remove(anEndpointName);
			}
			else
			{
				//System.out.println("!!!!!!!!!!!!!! Unable to Remove Endpoint from cache. Endpoint Name::"+anEndpointName+" Not In Cache");
			}
		}
		protected void deleteCAS()
		{
			cas = null;
			otsd = null;
		}
		public CAS getCas()
		{
			return cas;
		}
		protected void setCas(CAS aCAS, OutOfTypeSystemData aotsd)
		{
			cas = aCAS;
			otsd = aotsd;
		}

		protected void setCas(CAS aCAS)
		{
			cas = aCAS;
		}
		protected void setSerializedCas(String aSerializedCas)
		{
			serializedCas = aSerializedCas;
		}

		protected String getSerializedCas()
		{
			return serializedCas;
		}

		protected MessageContext getMessageAccessor()
		{
			return messageAccessor;
		}

		public OutOfTypeSystemData getOtsd()
		{
			return otsd;
		}
		public int getHighWaterMark()
		{
			return highWaterMark;
		}
		public void setHighWaterMark(int aHighWaterMark)
		{
			highWaterMark = aHighWaterMark;
		}
		public boolean isNewCas()
		{
			return newCas;
		}
		public void setNewCas(boolean newCas, String producedIn)
		{
			this.newCas = newCas;
			aggregateProducingTheCas = producedIn;
		}
		public XmiSerializationSharedData getDeserSharedData()
		{
			return deserSharedData;
		}
		
		public void setXmiSerializationData( XmiSerializationSharedData anXmiSerializationData)
		{
			deserSharedData = anXmiSerializationData;
		}

		public String getCasProducerAggregateName()
		{
			return aggregateProducingTheCas;
		}	
		
		public void setCasProducerKey( String aCasProducerKey )
		{
			casProducerKey = aCasProducerKey;
		}
		
		public String getCasProducerKey()
		{
			return casProducerKey;
		}
		public String getRemoteCMCasReferenceId()
		{
			return remoteCMCasReferenceId;
		}
		public void setRemoteCMCasReferenceId(String remoteCMCasReferenceId)
		{
			this.remoteCMCasReferenceId = remoteCMCasReferenceId;
		}
		public boolean shouldSendRequestToFreeCas()
		{
			return sendRequestToFreeCas;
		}
		public void setSendRequestToFreeCas(boolean sendRequestToFreeCas)
		{
			this.sendRequestToFreeCas = sendRequestToFreeCas;
		}
		public boolean isAborted()
		{
			return aborted;
		}
		public void setAborted(boolean aborted)
		{
			this.aborted = aborted;
		}
		public long getTimeToProcessCAS() {
			return timeToProcessCAS;
		}
		public void setTimeToProcessCAS(long timeToProcessCAS) {
			this.timeToProcessCAS = timeToProcessCAS;
		}

    public boolean isPendingReply() {
      return pendingReply;
    }
    public void setPendingReply(boolean pendingReply) {
      this.pendingReply = pendingReply;
    }

	}	


}
