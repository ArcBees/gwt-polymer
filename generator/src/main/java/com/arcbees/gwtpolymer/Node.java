/**
 * Copyright 2015 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.gwtpolymer;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private final T data;

    private Node<T> parent;
    private List<Node<T>> children;

    public Node(
            T data,
            Node<T> parent,
            List<Node<T>> children) {
        this.data = data;
        this.parent = parent;
        this.children = children;
    }

    public Node(T data) {
        this(data, null, new ArrayList<Node<T>>());
    }

    public T getData() {
        return data;
    }

    public Node<T> getParent() {
        return parent;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public void addChild(Node<T> child) {
        children.add(child);
    }
}
