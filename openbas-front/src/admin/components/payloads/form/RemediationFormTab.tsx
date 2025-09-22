import type { ClassicEditor } from 'ckeditor5';
import { useEffect, useRef, useState } from 'react';
import { Controller, useFormContext } from 'react-hook-form';

import { postDetectionRemediationAIRulesCrowdstrike } from '../../../../actions/detection-remediation/detectionremediation-action';
import CKEditor from '../../../../components/CKEditor';
import { type Collector, type PayloadInput } from '../../../../utils/api-types';
import { isNotEmptyField } from '../../../../utils/utils';
import { hasSpecificDirtyField, payloadFormToPayloadInputForAI } from '../utils/payloadFormToPayloadInput';
import typeChar from '../utils/typeChar';
import DetectionRemediationInfo from './DetectionRemediationInfo';
import DetectionRemediationUseAriane from './DetectionRemediationUseAriane';

interface RemediationFormTabProps {
  tab: Collector;
  activeTab: Collector;
}

const RemediationFormTab = ({
  tab,
  activeTab,
}: RemediationFormTabProps) => {
  const { control, watch, setValue, getValues, formState: { dirtyFields, isValid } } = useFormContext();
  const [isArianeTyping, setIsArianeTyping] = useState(false);
  const editorRef = useRef<ClassicEditor | null>(null);

  const fieldName = 'remediations.' + tab.collector_type;

  useEffect(() => {
    const current = getValues(fieldName);
    if (hasSpecificDirtyField(dirtyFields)) {
      setValue(fieldName, {
        ...current,
        author_rule: current.author_rule !== 'HUMAN' ? 'AI_OUTDATED' : current.author_rule,
      }, { shouldDirty: true });
    }
  }, [dirtyFields]);

  const onClickUseAriane = async () => {
    const payloadInput: Partial<PayloadInput> = payloadFormToPayloadInputForAI(getValues());
    return postDetectionRemediationAIRulesCrowdstrike(tab.collector_type, payloadInput).then((value) => {
      const editor = editorRef.current;
      if (editor) {
        setIsArianeTyping(true);
        typeChar(
          editor,
          value.data.rules,
          (value: string) => {
            const current = getValues(fieldName);
            const updated = {
              ...current,
              content: value,
            };
            setValue(fieldName, updated);
            setIsArianeTyping(false);
          },
        );
      }
    });
  };

  return (
    <>
      <div style={{
        display: 'flex',
        justifyContent: 'space-between',
      }}
      >
        <div>
          {isNotEmptyField(watch(fieldName)?.content)
            && <DetectionRemediationInfo author_rule={watch(fieldName).author_rule} />}
        </div>
        <DetectionRemediationUseAriane
          collectorType={tab.collector_type}
          detectionRemediationContent={watch(fieldName)?.content}
          onSubmit={onClickUseAriane}
          isValidForm={isValid}
        />
      </div>
      <div
        key={tab.collector_type}
        style={{
          height: '250px',
          position: 'relative',
          display: tab.collector_type === activeTab.collector_type ? 'block' : 'none',
        }}
      >
        <Controller
          name={fieldName}
          control={control}
          defaultValue={{ content: '' }}
          render={({ field: { onChange, value } }) => (
            <CKEditor
              onReady={editor => editorRef.current = editor}
              id={'payload-remediation-editor' + tab.collector_type}
              data={value?.content}
              onChange={(_, editor) => {
                const latest = getValues(fieldName);
                if (isArianeTyping) {
                  onChange({
                    ...latest,
                    content: editor.getData(),
                    author_rule: 'AI',
                  });
                } else {
                  onChange({
                    ...latest,
                    content: editor.getData(),
                    author_rule: 'HUMAN',
                  });
                }
              }}
            />
          )}
        />
      </div>
    </>
  );
};

export default RemediationFormTab;
