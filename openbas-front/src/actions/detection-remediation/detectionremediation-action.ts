import { simplePostCall } from '../../utils/Action';
import { type PayloadInput } from '../../utils/api-types';

const DETECTION_REMEDIATION_URI = '/api/detection-remediations/ai';

// eslint-disable-next-line import/prefer-default-export
export const postDetectionRemediationAIRulesCrowdstrike = (collectorType: string, payloadInput: Partial<PayloadInput>) => {
  const uri = `${DETECTION_REMEDIATION_URI}/rules/${collectorType}`;
  return simplePostCall(uri, payloadInput);
};
