/*******************************************************************************
 * Copyright (c) 2010 Verticon, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Verticon, Inc. - initial API and implementation
 *******************************************************************************/
package com.verticon.tracker.irouter.trutest;

import static com.verticon.tracker.irouter.common.TrackerConstants.CONNECTION_URI;
import static com.verticon.tracker.irouter.common.TrackerConstants.TRACKER_WIRE_GROUP_NAME;
import static com.verticon.tracker.irouter.trutest.Component.bundleMarker;
import static com.verticon.tracker.irouter.trutest.Constants.ANIMAL_WEIGHT;
import static com.verticon.tracker.irouter.trutest.Constants.EID;
import static com.verticon.tracker.irouter.trutest.Constants.PRODUCER_SCOPE;
import static com.verticon.tracker.irouter.trutest.Constants.RECORD_STATE;
import static org.osgi.framework.Constants.SERVICE_PID;
import static org.osgi.service.wireadmin.WireConstants.WIREADMIN_CONSUMER_PID;
import static org.osgi.service.wireadmin.WireConstants.WIREADMIN_PRODUCER_FLAVORS;
import static org.osgi.service.wireadmin.WireConstants.WIREADMIN_PRODUCER_SCOPE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.wireadmin.Envelope;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.Wire;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.verticon.tracker.irouter.common.IEnvelopeSender;

/**
 * 
 * A WireAdmin Producer that produces Envelopes Animal Weight Measurements, a EID States
 * and a Record States coming from a TruTest indicator.
 * @author jconlon
 */
public class EnvelopeProducer implements Producer, IEnvelopeSender {

	/**
	 * slf4j Logger
	 */
	private final Logger log = LoggerFactory
			.getLogger(EnvelopeProducer.class);


	private final IIndicator indicator;
	
	//Shared instances accessed by multiple threads protected with a concurrent collection
	private final List<Wire> wires = new CopyOnWriteArrayList<Wire>();
	private final Map<String, Envelope> lastEnvelope = new ConcurrentHashMap<String, Envelope>();
	
	
	private ServiceRegistration wireAdminReg = null;
	
	
	public EnvelopeProducer(IIndicator indicator) {
		super();
		this.indicator = indicator;;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EnvelopeProducer [pid="+indicator.getPid()+", uri=" + indicator.getConfigurationString(CONNECTION_URI) + "]";
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.service.wireadmin.Producer#consumersConnected(org.osgi.service.wireadmin.Wire[])
	 */
	@Override
	public void consumersConnected(Wire[] wires) {
		this.wires.clear();
		if(wires!=null){
			this.wires.addAll(Arrays.asList(wires));
		}
		if(this.wires.isEmpty()){
			
			log.debug(bundleMarker,"{}: Not connected to any wires.", this);
		}else{
			HashSet<String> consumers = new HashSet<String>();
			for (Wire wire : wires) {
				consumers.add((String)wire.getProperties().get(WIREADMIN_CONSUMER_PID));
			}
			
			log.debug(bundleMarker,"{}: Connected to {} wires, and {} consumers={}",
						new Object[]{this, wires.length, consumers.size() ,consumers}
			);
			
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.service.wireadmin.Producer#polled(org.osgi.service.wireadmin.Wire)
	 */
	@Override
	public Object polled(Wire wire) {
		String scopes[] = wire.getScope();
		for (String scope : scopes) {
			if (scope.equals(PRODUCER_SCOPE[ANIMAL_WEIGHT])) {
				return lastEnvelope.get(PRODUCER_SCOPE[ANIMAL_WEIGHT]);
			}
			if (scope.equals(PRODUCER_SCOPE[EID])) {
				return lastEnvelope.get(PRODUCER_SCOPE[EID]);
			}
			if (scope.equals(PRODUCER_SCOPE[RECORD_STATE])) {
				return lastEnvelope.get(PRODUCER_SCOPE[RECORD_STATE]);
			}
		}
		return null;
	}			

	
	/* (non-Javadoc)
	 * @see com.verticon.tracker.irouter.trutest.IEnvelopeSender#send(org.osgi.service.wireadmin.Envelope)
	 */
	public void send(Envelope envelope) {
		lastEnvelope.put(envelope.getScope(), envelope);
		log.debug(bundleMarker,"{}: Sending {} with scope={} to {} wires.",
					new Object[]{this, envelope.getValue(), envelope.getScope(), wires.size()}
		);
		
		for (Wire wire : wires) {
			String wireScopes[] =  wire.getScope();
			if(wireScopes.length==0){
				log.debug(bundleMarker,"{}: Defered sending {} wire has no scopes",
							this, envelope);
			}
			for (String wireScope : wireScopes) {
				if (wireScope.equals(envelope.getScope())) {

					wire.update(envelope);
				}
			}  
		}
		
	}
	
	/**
	 * Called by the Balance to unregister the service.
	 */
	void unRegister(){
		if(wireAdminReg != null){
			 wireAdminReg.unregister();
			 wireAdminReg=null;
		 }
	}
	
	
	/**
	 * Called by the balance to register this service.
	 * @param bc
	 */
    void register(BundleContext bc){
        
		Hashtable<String, Object> regProps = new Hashtable<String, Object>();
		regProps.put(SERVICE_PID, indicator.getPid());
		regProps.put(WIREADMIN_PRODUCER_SCOPE, 
				PRODUCER_SCOPE);
		regProps.put(WIREADMIN_PRODUCER_FLAVORS,
				new Class[] { Envelope.class });
		regProps.put(CONNECTION_URI, indicator.getConfigurationString(CONNECTION_URI));
		regProps.put(TRACKER_WIRE_GROUP_NAME, 
				indicator.getConfigurationString(TRACKER_WIRE_GROUP_NAME));
		wireAdminReg = bc.registerService(Producer.class
				.getName(), this, regProps);
	}

}
