/**
 *  Z-Wave Smart Fan Control
 * 
 *  A better functional Device Type for Z-Wave Smart Fan Control Switches
 *  Particularly the GE 12730 Z-Wave Smart Fan Control.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "v1a Smart Fan Control", namespace: "gdeardorff", author: "gdeardorff") {
		capability "Switch Level"
		capability "Actuator"
		capability "Indicator"
		capability "Switch"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
   		capability "Button"		

		command "lowSpeed"
		command "medSpeed"
		command "highSpeed"
        command "doubleUp"
        command "doubleDown"        

		attribute "currentState", "string"

		//fingerprint deviceId: "0x1101", inClusters: "0x26, 0x27, 0x70, 0x86, 0x72"
	}

	simulator {
		status "on":  "command: 2003, payload: FF"
		status "off": "command: 2003, payload: 00"
		status "09%": "command: 2003, payload: 09"
		status "10%": "command: 2003, payload: 0A"
		status "33%": "command: 2003, payload: 21"
		status "66%": "command: 2003, payload: 42"
		status "99%": "command: 2003, payload: 63"

		// reply messages
		reply "2001FF,delay 5000,2602": "command: 2603, payload: FF"
		reply "200100,delay 5000,2602": "command: 2603, payload: 00"
		reply "200119,delay 5000,2602": "command: 2603, payload: 19"
		reply "200132,delay 5000,2602": "command: 2603, payload: 32"
		reply "20014B,delay 5000,2602": "command: 2603, payload: 4B"
		reply "200163,delay 5000,2602": "command: 2603, payload: 63"
	}

	preferences {
		section("Fan Thresholds") {
			input "lowThreshold", "number", title: "Low Threshold", range: "1..99"
			input "medThreshold", "number", title: "Medium Threshold", range: "1..99"
			input "highThreshold", "number", title: "High Threshold", range: "1..99"
		}
	}

	tiles (scale:2) {
		multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
			tileAttribute ("device.currentState", key: "PRIMARY_CONTROL") {
				attributeState "default", label:'ADJUSTING', action:"refresh.refresh", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOff"
				attributeState "HIGH", label:'HIGH', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#486e13", nextState: "turningOff"
				attributeState "MED", label:'MED', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#60931a", nextState: "turningOff"
				attributeState "LOW", label:'LOW', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#79b821", nextState: "turningOff"
				attributeState "OFF", label:'OFF', action:"switch.on", icon:"st.Lighting.light24", backgroundColor:"#ffffff", nextState: "turningOn"
				attributeState "turningOn", action:"switch.on", label:'TURNINGON', icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOn"
				attributeState "turningOff", action:"switch.off", label:'TURNINGOFF', icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOff"
			}
			tileAttribute ("device.level", key: "SECONDARY_CONTROL") {
				attributeState "level", label:'${currentValue}%'
			}
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
		standardTile("lowSpeed", "device.currentState", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: 'LOW', action: "lowSpeed", icon:"st.Home.home30", backgroundColor: "#ffffff"
			state "LOW", label:'LOW', action: "lowSpeed", icon:"st.Home.home30", backgroundColor: "#79b821"
			state "ADJUSTING.LOW", label:'LOW', action: "lowSpeed", icon:"st.Home.home30", backgroundColor: "#2179b8"
  		}
		standardTile("medSpeed", "device.currentState", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: 'MED', action: "medSpeed", icon:"st.Home.home30", backgroundColor: "#ffffff"
			state "MED", label: 'MED', action: "medSpeed", icon:"st.Home.home30", backgroundColor: "#79b821"
			state "ADJUSTING.MED", label:'MED', action: "medSpeed", icon:"st.Home.home30", backgroundColor: "#2179b8"
		}
		standardTile("highSpeed", "device.currentState", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: 'HIGH', action: "highSpeed", icon:"st.Home.home30", backgroundColor: "#ffffff"
			state "HIGH", label: 'HIGH', action: "highSpeed", icon:"st.Home.home30", backgroundColor: "#79b821"
			state "ADJUSTING.HIGH", label:'HIGH', action: "highSpeed", icon:"st.Home.home30", backgroundColor: "#2179b8"
		}
		standardTile("indicator", "device.indicatorStatus", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "when off", action:"indicator.indicatorWhenOn", icon:"st.indicators.lit-when-off"
			state "when on", action:"indicator.indicatorNever", icon:"st.indicators.lit-when-on"
			state "never", action:"indicator.indicatorWhenOff", icon:"st.indicators.never-lit"
		}
        standardTile("doubleUp", "device.button", width: 3, height: 2, decoration: "flat") {
			state "default", label: "Tap ▲▲", backgroundColor: "#ffffff", action: "doubleUp", icon: "https://raw.githubusercontent.com/nuttytree/Nutty-SmartThings/master/devicetypes/nuttytree/SwitchOnIcon.png"
		}     
        standardTile("doubleDown", "device.button", width: 3, height: 2, decoration: "flat") {
			state "default", label: "Tap ▼▼", backgroundColor: "#ffffff", action: "doubleDown", icon: "https://raw.githubusercontent.com/nuttytree/Nutty-SmartThings/master/devicetypes/nuttytree/SwitchOffIcon.png"
		} 

		controlTile("levelSliderControl", "device.level", "slider", height: 2, width: 2, inactiveLabel: false) {
			state "level", action:"switch level.setLevel"
		}
		main(["switch"])
		details(["switch", "lowSpeed", "medSpeed", "highSpeed", "indicator", "levelSliderControl", "refresh", "doubleUp", "doubleDown"])
	}
}

def getCommandClassVersions() {
	[
		0x20: 1,  // Basic
		0x25: 1,  // SwitchBinary
//		0x26: 1,  // SwitchMultilevel
		0x26: 3,  // SwitchMultilevel
		0x56: 1,  // Crc16Encap
//		0x70: 1,  // Configuration
		0x70: 2,  // Configuration
        0x72: 2,  // Manufacturer Specific
        0x85: 2,  // Association
        // 26,2C,2B,27,56,59,5A,5E,70,72,73,7A,85,86
	]
}

def getCommandClassVersionsOrig() {
	[
		0x20: 1,  // Basic
		0x26: 1,  // SwitchMultilevel
		0x70: 1,  // Configuration
	]
}

def parse(String description) {
    log.debug "Parse description: $description"

	def item1 = [
		canBeCurrentState: false,
		linkText: getLinkText(device),
		isStateChange: false,
		displayed: false,
		descriptionText: description,
		value:  description
	]
	def result
	def cmd = zwave.parse(description, commandClassVersions)
	if (cmd) {
	    log.debug "Parsed Command:  ${cmd}"    
		result = createEvent(cmd, item1)
//		log.debug "createEvent Result: ${result?.inspect()}"        
	}
	else {
     	log.debug "Non-parsed event: ${description}"    
		item1.displayed = displayed(description, item1.isStateChange)
		result = [item1]
	}
	log.debug "Parse resulted in: ${result?.descriptionText}"
	result
}

def createEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd, Map item1) {
	log.trace "Start BasicReport"
	def result = doCreateEvent(cmd, item1)
	for (int i = 0; i < result.size(); i++) {
  	result[i].type = "physical"
	}
	log.trace "End BasicReport"
  result
}

def createEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd, Map item1) {
	log.trace "Start createEvent BasicSet"
    def result
    def level = 99
	if (cmd.value == 255) {
    	result = createEvent(name: "button", value: "pushed", data: [buttonNumber: 1], descriptionText: "Double-tap up (button 1) on $device.displayName", isStateChange: true, type: "physical")
	} else 	if (cmd.value == 0) {
    	result = createEvent(name: "button", value: "pushed", data: [buttonNumber: 2], descriptionText: "Double-tap down (button 2) on $device.displayName", isStateChange: true, type: "physical")
	}
	log.trace "End createEvent BasicSet"
	result
}

def createEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelStartLevelChange cmd, Map item1) {
	[]
	log.trace "StartLevel"
}

def createEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelStopLevelChange cmd, Map item1) {
	[response(zwave.basicV1.basicGet())]
}

def createEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelSet cmd, Map item1) {
	log.trace "Start SwitchMultiLevelSet"
	def result = doCreateEvent(cmd, item1)
	for (int i = 0; i < result.size(); i++) {
		result[i].type = "physical"
	}
	log.trace "End SwitchMultiLevelSet"
	result
}

def createEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd, Map item1) {
	log.trace "Start SwitchMultilevelReport v3"
	def result = doCreateEvent(cmd, item1)
	result[0].descriptionText = "${item1.linkText} is ${item1.value}"
	result[0].handlerName = cmd.value ? "statusOn" : "statusOff"
	for (int i = 0; i < result.size(); i++) {
		result[i].type = "digital"
	}
	log.trace "End SwitchMultilevelReport"
	result
}


def doCreateEvent(physicalgraph.zwave.Command cmd, Map item1) {
	log.trace "start doCreateEvent"

	def result = [item1]
	def lowThresholdvalue = (settings.lowThreshold != null && settings.lowThreshold != "") ? settings.lowThreshold.toInteger() : 33
	def medThresholdvalue = (settings.medThreshold != null && settings.medThreshold != "") ? settings.medThreshold.toInteger() : 67
	def highThresholdvalue = (settings.highThreshold != null && settings.highThreshold != "") ? settings.highThreshold.toInteger() : 99

	item1.name = "switch"
	item1.value = cmd.value ? "on" : "off"
	if (item1.value == "off") {
		sendEvent(name: "currentState", value: "OFF" as String)
	}
	item1.handlerName = item1.value
	item1.descriptionText = "${item1.linkText} was turned ${item1.value}"
	item1.canBeCurrentState = true
	item1.isStateChange = isStateChange(device, item1.name, item1.value)
	item1.displayed = false

	if (cmd.value) {
		def item2 = new LinkedHashMap(item1)
		item2.name = "level"
		item2.value = cmd.value as String
		item2.unit = "%"
		item2.descriptionText = "${item1.linkText} dimmed ${item2.value} %"
		item2.canBeCurrentState = true
		item2.isStateChange = isStateChange(device, item2.name, item2.value)
		item2.displayed = false

		if (item2.value.toInteger() <= lowThresholdvalue) { sendEvent(name: "currentState", value: "LOW" as String) }
		if (item2.value.toInteger() >= lowThresholdvalue+1 && item2.value.toInteger() <= medThresholdvalue) { sendEvent(name: "currentState", value: "MED" as String) }
		if (item2.value.toInteger() >= medThresholdvalue+1) { sendEvent(name: "currentState", value: "HIGH" as String) }

		result << item2
	}
	log.trace "end doCreateEvent"
	result
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv2.ConfigurationReport cmd) {
	def value = "when off"
	log.trace "ConfigurationReport"
	if (cmd.configurationValue[0] == 1) {value = "when on"}
	if (cmd.configurationValue[0] == 2) {value = "never"}
	[name: "indicatorStatus", value: value, display: false]
}

def createEvent(physicalgraph.zwave.Command cmd,  Map map) {
	// Handles any Z-Wave commands we aren't interested in
	log.debug "UNHANDLED COMMAND ${cmd}"
	log.debug "    ${cmd.getClass()}"
}
def createEvent(physicalgraph.zwave.Command cmd) {
	// Handles any Z-Wave commands we aren't interested in
	log.debug "UNHANDLED COMMAND NO MAP ${cmd}"
	log.debug "    ${cmd.getClass()}"
}

def on() {
	log.info "on"
	delayBetween([zwave.basicV1.basicSet(value: 0xFF).format(), zwave.switchMultilevelV3.switchMultilevelGet().format()], 1000)
}

def off() {
	log.info "off"
	delayBetween ([zwave.basicV1.basicSet(value: 0x00).format(), zwave.switchMultilevelV3.switchMultilevelGet().format()], 1000)
}

def setLevel(value) {
	def lowThresholdvalue = (settings.lowThreshold != null && settings.lowThreshold != "") ? settings.lowThreshold.toInteger() : 33
	def medThresholdvalue = (settings.medThreshold != null && settings.medThreshold != "") ? settings.medThreshold.toInteger() : 67
	def highThresholdvalue = (settings.highThreshold != null && settings.highThreshold != "") ? settings.highThreshold.toInteger() : 99
	
	if (value == "LOW") { value = lowThresholdvalue }
	if (value == "MED") { value = medThresholdvalue }
	if (value == "HIGH") { value = highThresholdvalue }

	def level = Math.min(value as Integer, 99)
	
	log.trace "setLevel(value): ${level}"
    
	if (level <= lowThresholdvalue) { sendEvent(name: "currentState", value: "ADJUSTING.LOW" as String, displayed: false) }
	if (level >= lowThresholdvalue+1 && level <= medThresholdvalue) { sendEvent(name: "currentState", value: "ADJUSTING.MED" as String, displayed: false) }
	if (level >= medThresholdvalue+1) { sendEvent(name: "currentState", value: "ADJUSTING.HIGH" as String, displayed: false) }
	
	delayBetween ([zwave.basicV1.basicSet(value: level as Integer).format(), zwave.switchMultilevelV3.switchMultilevelGet().format()], 1000)
}

def setLevel(value, duration) {
	def lowThresholdvalue = (settings.lowThreshold != null && settings.lowThreshold != "") ? settings.lowThreshold.toInteger() : 33
	def medThresholdvalue = (settings.medThreshold != null && settings.medThreshold != "") ? settings.medThreshold.toInteger() : 67
	def highThresholdvalue = (settings.highThreshold != null && settings.highThreshold != "") ? settings.highThreshold.toInteger() : 99

	if (value == "LOW") { value = lowThresholdvalue }
	if (value == "MED") { value = medThresholdvalue }
	if (value == "HIGH") { value = highThresholdvalue }

	def level = Math.min(value as Integer, 99)
	def dimmingDuration = duration < 128 ? duration : 128 + Math.round(duration / 60)
	
	log.trace "setLevel(value, duration): ${level}"
	
	if (level <= lowThresholdvalue) { sendEvent(name: "currentState", value: "ADJUSTING.LOW" as String, displayed: false) }
	if (level >= lowThresholdvalue+1 && level <= medThresholdvalue) { sendEvent(name: "currentState", value: "ADJUSTING.MED" as String, displayed: false) }
	if (level >= medThresholdvalue+1) { sendEvent(name: "currentState", value: "ADJUSTING.HIGH" as String, displayed: false) }
    
	zwave.switchMultilevelV3.switchMultilevelSet(value: level, dimmingDuration: dimmingDuration).format()
}

def lowSpeed() {
	setLevel("LOW")
}

def medSpeed() {
	setLevel("MED")
}

def highSpeed() {
	setLevel("HIGH")
}

def poll() {
/*
	zwave.switchMultilevelV1.switchMultilevelGet().format()
    
    def cmds = []
    cmds << zwave.switchMultilevelV2.switchMultilevelGet().format()
	if (getDataValue("MSR") == null) {
		cmds << zwave.manufacturerSpecificV1.manufacturerSpecificGet().format()
	}
*/
	def cmds = []
    cmds << zwave.switchMultilevelV3.switchMultilevelGet().format()
	if (getDataValue("MSR") == null) {
		cmds << zwave.manufacturerSpecificV2.manufacturerSpecificGet().format()
	}
   	delayBetween(cmds,500)

}

/*
def refresh() {
	zwave.switchMultilevelV1.switchMultilevelGet().format()
}
*/

def refresh() {
	def cmds = []
	cmds << zwave.switchMultilevelV3.switchMultilevelGet().format()
    cmds << zwave.configurationV2.configurationGet(parameterNumber: 3).format()
    cmds << zwave.configurationV2.configurationGet(parameterNumber: 4).format()
    cmds << zwave.associationV2.associationGet(groupingIdentifier: 3).format()
	if (getDataValue("MSR") == null) {
		cmds << zwave.manufacturerSpecificV1.manufacturerSpecificGet().format()
	}
	delayBetween(cmds,500)
}


def indicatorWhenOn() {
	sendEvent(name: "indicatorStatus", value: "when on", display: false)
	zwave.configurationV2.configurationSet(configurationValue: [1], parameterNumber: 3, size: 1).format()
}

def indicatorWhenOff() {
	sendEvent(name: "indicatorStatus", value: "when off", display: false)
	zwave.configurationV2.configurationSet(configurationValue: [0], parameterNumber: 3, size: 1).format()
}

def indicatorNever() {
	sendEvent(name: "indicatorStatus", value: "never", display: false)
	zwave.configurationV2.configurationSet(configurationValue: [2], parameterNumber: 3, size: 1).format()
}

def doubleUp() {
	log.trace "doubleUp()"

	def level = device.currentState("level").value.toInteger()
	def low = settings.lowThreshold.toInteger()
	def med = settings.medThreshold.toInteger()
	def high = settings.highThreshold.toInteger()
    
	if (level < low) { 
		setLevel("LOW")
	} else if (level >= low && level < med) { 
		setLevel("MED")
    } else if (level >= med && level < high) { 
		setLevel("HIGH")
    } else {
    	log.trace "No Change Made"
    }

}

def doubleDown() {
	log.trace "doubleDown()"

	def level = device.currentState("level").value.toInteger()
	def low = settings.lowThreshold.toInteger()
	def med = settings.medThreshold.toInteger()
	def high = settings.highThreshold.toInteger()
    
	if (level > low && level <= med) { 
		setLevel("LOW")
    } else if (level > med && level <= high) { 
		setLevel("MED")
    } else if (level > high) { 
		setLevel("HIGH")
    } else {
    	log.trace "No Change Made"
    }
}


def zwaveEvent(physicalgraph.zwave.commands.crc16encapv1.Crc16Encap cmd) {
	log.debug("zwaveEvent(): CRC-16 Encapsulation Command received: ${cmd}")
	def encapsulatedCommand = zwave.commandClass(cmd.commandClass)?.command(cmd.command)?.parse(cmd.data)
	if (!encapsulatedCommand) {
		log.warn("zwaveEvent(): Could not extract command from ${cmd}")
	} else {
		log.debug("zwaveEvent(): Extracted command ${encapsulatedCommand}")
        return zwaveEvent(encapsulatedCommand)
	}
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
	log.trace "Start basicv1.BasicReport"
	dimmerEvents(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd) {
	log.trace "Start switchmultilevelv3.SwitchMultilevelReport"
	dimmerEvents(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelSet cmd) {
	log.trace "Start switchmultilevelv3.SwitchMultilevelSet"
	dimmerEvents(cmd)
}

private dimmerEvents(physicalgraph.zwave.Command cmd) {
	log.trace "Start dimmerEvents"
	def value = (cmd.value ? "on" : "off")
	def result = [createEvent(name: "switch", value: value, type: "physical")]
	if (cmd.value && cmd.value <= 100) {
		result << createEvent(name: "level", value: cmd.value, unit: "%", type: "physical")
	}
	return result
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
	log.trace "Start BasicSet 2"
	if (cmd.value == 255) {
  //  	delayBetween ([zwave.basicV1.basicSet(value: "99" as Integer).format(), zwave.switchMultilevelV3.switchMultilevelGet().format()], 1000)

//    	doubleUp();
//    	createEvent(name: "button", value: "pushed", data: [buttonNumber: 1], descriptionText: "Double-tap up (button 1) on $device.displayName", isStateChange: true, type: "physical")
    }
	else if (cmd.value == 0) {
    	doubleDown();
//    	createEvent(name: "button", value: "pushed", data: [buttonNumber: 2], descriptionText: "Double-tap down (button 2) on $device.displayName", isStateChange: true, type: "physical")
    }
}

def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
    log.debug "---MANUFACTURER SPECIFIC REPORT V2---"
	log.debug "manufacturerId:   ${cmd.manufacturerId}"
	log.debug "manufacturerName: ${cmd.manufacturerName}"
    state.manufacturer=cmd.manufacturerName
	log.debug "productId:        ${cmd.productId}"
	log.debug "productTypeId:    ${cmd.productTypeId}"
	def msr = String.format("%04X-%04X-%04X", cmd.manufacturerId, cmd.productTypeId, cmd.productId)
	updateDataValue("MSR", msr)	
    sendEvent([descriptionText: "$device.displayName MSR: $msr", isStateChange: false])
}

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationReport cmd) {
	log.debug "---ASSOCIATION REPORT V2--- ${device.displayName} sent groupingIdentifier: ${cmd.groupingIdentifier} maxNodesSupported: ${cmd.maxNodesSupported} nodeId: ${cmd.nodeId} reportsToFollow: ${cmd.reportsToFollow}"
    state.group3 = "1,2"
    if (cmd.groupingIdentifier == 3) {
    	if (cmd.nodeId.contains(zwaveHubNodeId)) {
        	createEvent(name: "numberOfButtons", value: 2, displayed: false)
        }
        else {
        	sendEvent(name: "numberOfButtons", value: 0, displayed: false)
			sendHubCommand(new physicalgraph.device.HubAction(zwave.associationV2.associationSet(groupingIdentifier: 3, nodeId: zwaveHubNodeId).format()))
			sendHubCommand(new physicalgraph.device.HubAction(zwave.associationV2.associationGet(groupingIdentifier: 3).format()))
        }
    }
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
    log.debug("sceneNumber: ${cmd.sceneNumber} keyAttributes: ${cmd.keyAttributes}")
    def result = []
    
    switch (cmd.sceneNumber) {
      case 1:
          // Up
          switch (cmd.keyAttributes) {
              case 0:
                   // Press Once
                  result += createEvent(tapUp1Response("physical"))  
                  result += createEvent([name: "switch", value: "on", type: "physical"])
       
                  if (singleTapToFullBright)
                  {
                     result += setLevel(99)
                     result += response("delay 5000")
                     result += response(zwave.switchMultilevelV3.switchMultilevelGet())
                  } 
                  break
              case 1:
                  result=createEvent([name: "switch", value: "on", type: "physical"])
                  break
              case 2:
                  // Hold
                  result += createEvent(holdUpResponse("physical"))  
                  result += createEvent([name: "switch", value: "on", type: "physical"])    
                  break
              case 3: 
                  // 2 Times
                  result +=createEvent(tapUp2Response("physical"))
                  if (doubleTapToFullBright)
                  {
                     result += setLevel(99)
                     result += response("delay 5000")
                     result += response(zwave.switchMultilevelV3.switchMultilevelGet())
                  }                    
                  break
              case 4:
                  // 3 times
                  result=createEvent(tapUp3Response("physical"))
                  break
              case 5:
                  // 4 times
                  result=createEvent(tapUp4Response("physical"))
                  break
              case 6:
                  // 5 times
                  result=createEvent(tapUp5Response("physical"))
                  break
              default:
                  log.debug ("unexpected up press keyAttribute: $cmd.keyAttributes")
          }
          break
          
      case 2:
          // Down
          switch (cmd.keyAttributes) {
              case 0:
                  // Press Once
                  result += createEvent(tapDown1Response("physical"))
                  result += createEvent([name: "switch", value: "off", type: "physical"]) 
                  break
              case 1:
                  result=createEvent([name: "switch", value: "off", type: "physical"])
                  break
              case 2:
                  // Hold
                  result += createEvent(holdDownResponse("physical"))
                  result += createEvent([name: "switch", value: "off", type: "physical"]) 
                  break
              case 3: 
                  // 2 Times
                  result+=createEvent(tapDown2Response("physical"))
                  if (doubleTapDownToDim)
                  {
                     result += setLevel(25)
                     result += response("delay 5000")
                     result += response(zwave.switchMultilevelV3.switchMultilevelGet())
                  }  
                  break
              case 4:
                  // 3 Times
                  result=createEvent(tapDown3Response("physical"))
                  break
              case 5:
                  // 4 Times
                  result=createEvent(tapDown4Response("physical"))
                  break
              case 6:
                  // 5 Times
                  result=createEvent(tapDown5Response("physical"))
                  break
              default:
                  log.debug ("unexpected down press keyAttribute: $cmd.keyAttributes")
           } 
           break
           
      default:
           // unexpected case
           log.debug ("unexpected scene: $cmd.sceneNumber")
   }  
   return result
}

def eventHandler(evt) {
    // get the event name, e.g., "switch"
    log.debug "This event name is ${evt.name}"

    // get the value of this event, e.g., "on" or "off"
    log.debug "The value of this event is ${evt.value}"

    // get the Date this event happened at
    log.debug "This event happened at ${evt.date}"

    // did the value of this event change from its previous state?
    log.debug "The value of this event is different from its previous value: ${evt.isStateChange()}"
}
