/**
 * BlueCove BlueZ module - Java library for Bluetooth on Linux
 *  Copyright (C) 2008 Mina Shokry
 *  Copyright (C) 2008 Vlad Skarzhevskyy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @version $Id: BlueCoveBlueZ_SDPServer.c 1728 2008-01-31 18:54:46Z skarzhevskyy $
 */
#define CPP__FILE "BlueCoveBlueZ_SDPServer.c"

#include "BlueCoveBlueZ.h"

#include <bluetooth/sdp_lib.h>

// Since bluez-libs-3.8 we have sdp_device_record_register_binary
//#define BLUECOVE_USE_BINARY_SDP

JNIEXPORT jlong JNICALL Java_com_intel_bluetooth_BluetoothStackBlueZ_openSDPSessionImpl
  (JNIEnv* env, jobject peer) {
    sdp_session_t* session = sdp_connect(BDADDR_ANY, BDADDR_LOCAL, SDP_RETRY_IF_BUSY);
    if (!session) {
        throwServiceRegistrationException(env, "Can not open SDP session. [%d] %s", errno, strerror(errno));
        return 0;
    }
    return ptr2jlong(session);
}

JNIEXPORT void JNICALL Java_com_intel_bluetooth_BluetoothStackBlueZ_closeSDPSessionImpl
  (JNIEnv* env, jobject peer, jlong sdpSessionHandle, jboolean quietly) {
    if (sdpSessionHandle == 0) {
        return;
    }
    if (sdp_close((sdp_session_t*)jlong2ptr(sdpSessionHandle)) < 0) {
        if (quietly) {
            throwServiceRegistrationException(env, "Failed to close SDP session. [%d] %s", errno, strerror(errno));
        } else {
            debug("Failed to close SDP session. [%d] %s", errno, strerror(errno));
        }
    }
}

sdp_record_t* createNativeSDPrecord(JNIEnv* env, jbyteArray record) {
    int length = (*env)->GetArrayLength(env, record);
    int MAX_BLUEZ_PDU = SDP_REQ_BUFFER_SIZE - sizeof(sdp_pdu_hdr_t) - sizeof(bdaddr_t) - 2;
    if (length > MAX_BLUEZ_PDU) {
        throwServiceRegistrationException(env, "SDP record too large %i of max %i", length, MAX_BLUEZ_PDU);
        return NULL;
    }
    jbyte *bytes = (*env)->GetByteArrayElements(env, record, 0);
    int length_scanned = length;
    sdp_record_t *rec = sdp_extract_pdu((uint8_t*)bytes, &length_scanned);
    debug("pdu scanned %i -> %i", length, length_scanned);
    if (rec == NULL) {
        throwServiceRegistrationException(env, "Can not convert SDP record. [%d] %s", errno, strerror(errno));
    }
    (*env)->ReleaseByteArrayElements(env, record, bytes, 0);
    return rec;
}

JNIEXPORT jlong JNICALL Java_com_intel_bluetooth_BluetoothStackBlueZ_registerSDPServiceImpl
  (JNIEnv* env, jobject peer, jlong sdpSessionHandle, jlong localDeviceBTAddress, jbyteArray record) {
    sdp_session_t* session = (sdp_session_t*)jlong2ptr(sdpSessionHandle);
    sdp_record_t *rec = createNativeSDPrecord(env, record);
    if (rec == NULL) {
        return 0;
    }
    bdaddr_t localAddr;
    longToDeviceAddr(localDeviceBTAddress, &localAddr);
    // Remove ServiceRecordHandle
    sdp_attr_remove(rec, 0);
    rec->handle = 0;
    int flags = 0;
    int err = sdp_device_record_register(session, &localAddr, rec, flags);
    if (err != 0) {
        throwServiceRegistrationException(env, "Can not register SDP record. [%d] %s", errno, strerror(errno));
    }
    jlong handle = rec->handle;
    sdp_record_free(rec);
    return handle;
}

JNIEXPORT void JNICALL Java_com_intel_bluetooth_BluetoothStackBlueZ_updateSDPServiceImpl
  (JNIEnv* env, jobject peer, jlong sdpSessionHandle, jlong localDeviceBTAddress, jlong handle, jbyteArray record) {
    sdp_session_t* session = (sdp_session_t*)jlong2ptr(sdpSessionHandle);
    sdp_record_t *rec = createNativeSDPrecord(env, record);
    if (rec == NULL) {
        return;
    }
    bdaddr_t localAddr;
    longToDeviceAddr(localDeviceBTAddress, &localAddr);
    rec->handle = handle;
    int err = sdp_device_record_update(session, &localAddr, rec);
    if (err != 0) {
        throwServiceRegistrationException(env, "Can not update SDP record. [%d] %s", errno, strerror(errno));
    }
    sdp_record_free(rec);
}

JNIEXPORT void JNICALL Java_com_intel_bluetooth_BluetoothStackBlueZ_unregisterSDPServiceImpl
  (JNIEnv* env, jobject peer, jlong sdpSessionHandle, jlong localDeviceBTAddress, jlong handle, jbyteArray record) {
    sdp_session_t* session = (sdp_session_t*)jlong2ptr(sdpSessionHandle);
    sdp_record_t *rec;
    // Use just handle to unredister record
    //rec = createNativeSDPrecord(env, record);
    rec = sdp_record_alloc();
    if (rec == NULL) {
        return;
    }
    rec->handle = handle;
    bdaddr_t localAddr;
    longToDeviceAddr(localDeviceBTAddress, &localAddr);
    int err = sdp_device_record_unregister(session, &localAddr, rec);
    if (err != 0) {
        throwServiceRegistrationException(env, "Can not unregister SDP record. [%d] %s", errno, strerror(errno));
        sdp_record_free(rec);
    }
}

