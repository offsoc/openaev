import { type FieldNamesMarkedBoolean, type FieldValues } from 'react-hook-form';

import { type PayloadInput } from '../../../../utils/api-types';

// eslint-disable-next-line import/prefer-default-export
export const payloadFormToPayloadInputForAI = (data: FieldValues): Partial<PayloadInput> => {
  return {
    payload_name: data.payload_name,
    payload_type: data.payload_type,
    dns_resolution_hostname: data.dns_resolution_hostname,
    command_executor: data.command_executor,
    command_content: data.command_content,
    payload_description: data.payload_description,
    payload_platforms: data.payload_platforms,
    payload_execution_arch: data.payload_execution_arch,
    payload_arguments: data.payload_arguments,
    payload_attack_patterns: data.payload_attack_patterns,
  };
};

const trackedFields = [
  'payload_name',
  'payload_type',
  'dns_resolution_hostname',
  'command_executor',
  'command_content',
  'payload_description',
  'payload_platforms',
  'payload_execution_arch',
  'payload_arguments',
  'payload_attack_patterns',
];

export const hasSpecificDirtyField = (
  dirtyFields: Partial<Readonly<FieldNamesMarkedBoolean<FieldValues>>>,
): boolean => {
  return Object.keys(dirtyFields).some(field => trackedFields.includes(field));
};
