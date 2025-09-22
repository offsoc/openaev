import { type ClassicEditor } from 'ckeditor5';

const typeChar = (
  editor: ClassicEditor,
  submittedText: string,
  onComplete: (value: string) => void,
) => {
  const lines = submittedText.split(/\n/);
  let index = 0;
  let buffer = '';

  const typeNext = () => {
    if (index < lines.length) {
      const line = lines[index];
      buffer += line + '\n';
      editor.setData(buffer);

      const editingView = editor.editing.view;
      const domRoot = editingView.getDomRoot();
      if (domRoot) {
        domRoot.scrollTop = domRoot.scrollHeight;
      }
      index++;
      setTimeout(typeNext, 150);
    } else {
      onComplete(buffer);
    }
  };

  if (submittedText) {
    typeNext();
  }
};

export default typeChar;
