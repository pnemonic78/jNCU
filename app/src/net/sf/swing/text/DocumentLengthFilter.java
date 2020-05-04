/*
 * Copyright 2010, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.swing.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * Document filter that has a maximum length to limit the number of characters.
 *
 * @author Moshe
 */
public class DocumentLengthFilter extends DocumentFilter {

    private final int maxLength;

    /**
     * Create a new filter.
     *
     * @param length the maximum length.
     */
    public DocumentLengthFilter(int length) {
        this.maxLength = length;
    }

    /**
     * Get the maximum length.
     *
     * @return the maximum length.
     */
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text,
                             AttributeSet attr) throws BadLocationException {
        if (text == null)
            return;
        Document doc = fb.getDocument();
        int docLength = doc.getLength();
        if (docLength + text.length() > maxLength)
            return;
        super.insertString(fb, offset, text, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {
        if (text != null) {
            Document doc = fb.getDocument();
            int docLength = doc.getLength();
            if (docLength - length + text.length() > maxLength)
                return;
        }
        super.replace(fb, offset, length, text, attrs);
    }
}
