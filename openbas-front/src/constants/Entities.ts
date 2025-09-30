// General Names

export const INJECT = 'INJECT';
export const SIMULATION = 'SIMULATION';
export const SCENARIO = 'SCENARIO';

// Collectors
export const CROWDSTRIKE = 'openbas_crowdstrike';
export const DEFENDER = 'openbas_microsoft_defender';
export const SENTINEL = 'openbas_microsoft_sentinel';
export const SPLUNK = 'openbas_splunk_es';

export const COLLECTOR_LIST = [CROWDSTRIKE, SPLUNK, DEFENDER, SENTINEL];
export const COLLECTOR_LIST_AI = [CROWDSTRIKE, SPLUNK];
export const PAYLOAD_TYPE_LIST_AI = ['DnsResolution', 'Command'];
