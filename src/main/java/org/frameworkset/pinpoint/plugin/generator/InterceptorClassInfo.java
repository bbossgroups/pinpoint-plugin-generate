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

public class InterceptorClassInfo {
	private List<MethodInfo> interceptorMehtods ;
	private String interceptorClass;
	private MethodInfo allAccept ;
	private MethodInfo allReject ;

	public List<MethodInfo> getInterceptorMehtods() {
		return interceptorMehtods;
	}

	public void setInterceptorMehtods(List<MethodInfo> interceptorMehtods) {
		this.interceptorMehtods = interceptorMehtods;
	}

	public String getInterceptorClass() {
		return interceptorClass;
	}

	public void setInterceptorClass(String interceptorClass) {
		this.interceptorClass = interceptorClass;
	}

	public MethodInfo getAllReject() {
		return allReject;
	}

	public void setAllReject(MethodInfo allReject) {
		this.allReject = allReject;
	}

	public MethodInfo getAllAccept() {
		return allAccept;
	}

	public void setAllAccept(MethodInfo allAccept) {
		this.allAccept = allAccept;
	}
}
