package org.frameworkset.pinpoint.plugin.generator;
/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.util.List;

public class GenConfig {
	public static final String pluginInterceptorSpanTraceType = "spantrace";
	public static final String pluginInterceptorSpanEventType = "spanevent";
	/**
	 * ## 插件采集的链路日志类型：
	 * ## spanevent，上下文环境没有trace，丢弃日志数据，有trace则加入其中，日志被记录
	 * ## spantrace(默认值), 上下文环境没有trace，创建trace并记录日志数据，有trace则加入其中
	 * ##
	 */
	private String pluginInterceptorType = pluginInterceptorSpanTraceType;
	private int argKeyCode ;
	private String argKeyName;
	private String executionPolicy;
	private boolean cleanSource = true;
	private String serviceName;
	private String servicePackage;
	private int serviceType;
	private int eventServiceType;
	private boolean recordResult;
	private boolean recordArgs;
	private String pluginName;
	private String pluginVersion;
	private boolean pluginEnabled ;
	private String pluginAuthor;
	private String originIntercepteClasses;
	private List<InterceptorClassInfo> intercepteClasses;
	private String pluginSrcFileDir;
	private String metaInfServicesProfilerPluginFileDir = "src/main/resources/META-INF/services/";
	private String metaInfServicesProfilerPluginFileName = "com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin";
	private String metaInfServicesTraceMetadataProviderFileName = "com.navercorp.pinpoint.common.trace.TraceMetadataProvider";

	public String getOriginIntercepteClasses() {
		return originIntercepteClasses;
	}

	public void setOriginIntercepteClasses(String originIntercepteClasses) {
		this.originIntercepteClasses = originIntercepteClasses;
	}

	public String getPluginSrcFileDir() {
		return pluginSrcFileDir;
	}

	public void setPluginSrcFileDir(String pluginSrcFileDir) {
		this.pluginSrcFileDir = pluginSrcFileDir;
	}

	public String getMetaInfServicesProfilerPluginFileDir() {
		return metaInfServicesProfilerPluginFileDir;
	}

	public void setMetaInfServicesProfilerPluginFileDir(String metaInfServicesProfilerPluginFileDir) {
		this.metaInfServicesProfilerPluginFileDir = metaInfServicesProfilerPluginFileDir;
	}

	public String getMetaInfServicesProfilerPluginFileName() {
		return metaInfServicesProfilerPluginFileName;
	}

	public void setMetaInfServicesProfilerPluginFileName(String metaInfServicesProfilerPluginFileName) {
		this.metaInfServicesProfilerPluginFileName = metaInfServicesProfilerPluginFileName;
	}

	public String getMetaInfServicesTraceMetadataProviderFileName() {
		return metaInfServicesTraceMetadataProviderFileName;
	}

	public void setMetaInfServicesTraceMetadataProviderFileName(String metaInfServicesTraceMetadataProviderFileName) {
		this.metaInfServicesTraceMetadataProviderFileName = metaInfServicesTraceMetadataProviderFileName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}

	public boolean isRecordResult() {
		return recordResult;
	}

	public void setRecordResult(boolean recordResult) {
		this.recordResult = recordResult;
	}

	public boolean isRecordArgs() {
		return recordArgs;
	}

	public void setRecordArgs(boolean recordArgs) {
		this.recordArgs = recordArgs;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public boolean isPluginEnabled() {
		return pluginEnabled;
	}

	public void setPluginEnabled(boolean pluginEnabled) {
		this.pluginEnabled = pluginEnabled;
	}

	public String getPluginAuthor() {
		return pluginAuthor;
	}

	public void setPluginAuthor(String pluginAuthor) {
		this.pluginAuthor = pluginAuthor;
	}

	public List<InterceptorClassInfo> getIntercepteClasses() {
		return intercepteClasses;
	}

	public void setIntercepteClasses(List<InterceptorClassInfo> intercepteClasses) {
		this.intercepteClasses = intercepteClasses;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public String getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	public boolean isCleanSource() {
		return cleanSource;
	}

	public void setCleanSource(boolean cleanSource) {
		this.cleanSource = cleanSource;
	}

	public String getPluginInterceptorType() {
		return pluginInterceptorType;
	}

	public void setPluginInterceptorType(String pluginInterceptorType) {
		this.pluginInterceptorType = pluginInterceptorType;
	}

	public int getArgKeyCode() {
		return argKeyCode;
	}

	public void setArgKeyCode(int argKeyCode) {
		this.argKeyCode = argKeyCode;
	}

	public String getArgKeyName() {
		return argKeyName;
	}

	public void setArgKeyName(String argKeyName) {
		this.argKeyName = argKeyName;
	}

	public String getExecutionPolicy() {
		return executionPolicy;
	}

	public void setExecutionPolicy(String executionPolicy) {
		this.executionPolicy = executionPolicy;
	}

	public int getEventServiceType() {
		return eventServiceType;
	}

	public void setEventServiceType(int eventServiceType) {
		this.eventServiceType = eventServiceType;
	}
}
